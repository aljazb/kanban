import {ApiService} from '../api.service';
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

  getUserRequests(): Observable<Request[]> {
    return this.api.httpClient.get<Request[]>(`${this.url}/userRequests`).pipe(
      catchError(this.handleError<Request[]>("userRequests"))
    );
  }

  accept (requestId: string): Observable<Request> {
    return this.api.httpClient.put<Request>(this.url + "/" + requestId, null, { headers: this.getHeaders(true)})
      .pipe(
        catchError(this.handleError<Request>(`accept`))
      );
  }

  decline (requestId: string): Observable<Request> {
    return this.api.httpClient.delete<Request>(this.url + "/" + requestId, { headers: this.getHeaders(true)})
      .pipe(
        catchError(this.handleError<Request>(`decline`))
      );
  }

  createKanbanMasterInvite(devTeamId: string, userId: string, context: string): Observable<Request> {
    let request: Request = new Request();
    request.requestType = RequestType.KANBAN_MASTER_INVITE;
    request.referenceId = devTeamId;
    request.receiver = new UserAccount();
    request.receiver.id = userId;
    request.context = context;

    console.log(request);

    return this.api.httpClient.post<Request>(this.url, request, { headers: this.getHeaders()})
      .pipe(
        catchError(this.handleError<Request>(`createKanbanMasterInvite`))
      );
  }
}
