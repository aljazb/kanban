import {ApiService} from '../Api';
import {Request} from '../models/Request';
import {GetResource} from './base/GetResource';
import {Observable} from 'rxjs/Observable';
import {catchError} from 'rxjs/operators';
import {RequestType} from '../models/enums/RequestType';
import {UserAccount} from '../models/UserAccount';

export class RequestResource extends GetResource<Request> {

  constructor(api: ApiService) {
    super("Request", api);
  }

  accept (requestId: string): Observable<Request> {
    return this.api.httpClient.put<Request>(this.url + "/" + requestId, { headers: this.getHeaders()})
      .pipe(
        catchError(this.handleError<Request>(`accept`))
      );
  }

  decline (requestId: string): Observable<Request> {
    return this.api.httpClient.delete<Request>(this.url + "/" + requestId, { headers: this.getHeaders()})
      .pipe(
        catchError(this.handleError<Request>(`decline`))
      );
  }

  createDevTeamInvite(devTeamId: string, userId: string): Observable<Request> {
    let request: Request = new Request();
    request.requestType = RequestType.DEV_TEAM_INVITE;
    request.referenceId = devTeamId;
    request.receiver = new UserAccount();
    request.receiver.id = userId;

    return this.api.httpClient.post<Request>(this.url, { headers: this.getHeaders()})
      .pipe(
        catchError(this.handleError<Request>(`createDevTeamInvite`))
      );
  }

  createKanbanMasterPromotion(devTeamId: string, userId: string): Observable<Request> {
    let request: Request = new Request();
    request.requestType = RequestType.DEV_TEAM_KAMBAN_MASTER_PROMOTION;
    request.referenceId = devTeamId;
    request.receiver = new UserAccount();
    request.receiver.id = userId;

    return this.api.httpClient.post<Request>(this.url, { headers: this.getHeaders()})
      .pipe(
        catchError(this.handleError<Request>(`createKanbanMasterPromotion`))
      );
  }

}
