import {ApiService} from '../api.service';
import {Request} from '../models/Request';
import {GetResource} from './base/GetResource';
import {Observable} from 'rxjs/Observable';
import {catchError, map} from 'rxjs/operators';
import {RequestType} from '../models/enums/RequestType';
import {UserAccount} from '../models/UserAccount';

export class RequestResource extends GetResource<Request> {

  constructor(api: ApiService) {
    super("Request", api);
  }

  getUserRequests(): Observable<Request[]> {
    return this.api.httpClient.get<Request[]>(`${this.url}/userRequests`)
      .pipe(map(content => this.deserializeArray(content)));
  }

  accept (requestId: string): Observable<Request> {
    return this.api.httpClient.put<Request>(this.url + "/" + requestId, null, { headers: this.getHeaders(true)})
      .pipe(map(content => this.deserialize(content)));
  }

  decline (requestId: string): Observable<Request> {
    return this.api.httpClient.delete<Request>(this.url + "/" + requestId, { headers: this.getHeaders(true)})
      .pipe(map(content => this.deserialize(content)));
  }

  createKanbanMasterInvite(devTeamId: string, userId: string, context: string): Observable<Request> {
    let request: Request = new Request();
    request.requestType = RequestType.KANBAN_MASTER_INVITE;
    request.referenceId = devTeamId;
    request.receiver = new UserAccount();
    request.receiver.id = userId;
    request.context = context;

    return this.api.httpClient.post<Request>(this.url, this.serialize(request), { headers: this.getHeaders()})
      .pipe(map(content => this.deserialize(content)));
  }
}
