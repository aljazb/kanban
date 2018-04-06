import {UserAccount} from '../models/UserAccount';
import {Observable} from 'rxjs/Observable';
import {ApiService} from '../api.service';
import {catchError, map} from 'rxjs/operators';
import {GetResource} from './base/GetResource';

export class UserAccountResource extends GetResource<UserAccount> {

  constructor(api: ApiService) {
    super("UserAccount", api);
  }

  login (): Observable<UserAccount> {
    return this.api.httpClient.get<UserAccount>(this.url + "/login", { headers: this.getHeaders() })
      .pipe(
        catchError(this.handleError<UserAccount>(`login`)),
        map(content => this.deserialize(content))
      );
  }

  post (entity: UserAccount, xContent = this.api.xContent): Observable<UserAccount> {
    return this.api.httpClient.post<UserAccount>(this.url, entity, { headers: this.getHeaders(xContent) })
      .pipe(
        catchError(this.handleError<UserAccount>(`post`)),
        map(content => this.deserialize(content))
      );
  }

  put (entity: UserAccount, xContent = this.api.xContent): Observable<UserAccount> {
    return this.api.httpClient.put<UserAccount>(this.url + "/" + entity.id, entity, { headers: this.getHeaders(xContent) })
      .pipe(
        catchError(this.handleError<UserAccount>(`patch`)),
        map(content => this.deserialize(content))
      );
  }

  setPassword (id: string, password: string, xContent=this.api.xContent): Observable<UserAccount> {
    return this.api.httpClient.put<UserAccount>(this.url + "/" + id + "/password", password, { headers: this.getHeaders(xContent) })
      .pipe(
        catchError(this.handleError<UserAccount>(`patch`)),
        //map(content => this.deserialize(content))
      );
  }

  delete (id: string, xContent = this.api.xContent): Observable<UserAccount> {
    return this.api.httpClient.delete<UserAccount>(this.url + "/" + id, { headers: this.getHeaders(xContent) })
      .pipe(
        catchError(this.handleError<UserAccount>(`delete`)),
        map(content => this.deserialize(content))
      );
  }

  changeStatus (id: string, xContent = this.api.xContent): Observable<UserAccount> {
    return this.api.httpClient.put<UserAccount>(this.url + "/" + id + "/status/", null,{ headers: this.getHeaders(xContent) })
      .pipe(
        catchError(this.handleError<UserAccount>(`status`)),
        map(content => this.deserialize(content))
      );
  }

  getKanbanMasters(): Observable<UserAccount[]> {
    return this.api.httpClient.get<UserAccount[]>(this.url + "?where=inRoleKanbanMaster:eq:true",{ headers: this.getHeaders()})
      .pipe(
        catchError(this.handleError<UserAccount[]>(`get kanbanMasters`)),
        map(content => this.deserializeArray(content))
      );
  }

  getProductOwners(): Observable<UserAccount[]> {
    return this.api.httpClient.get<UserAccount[]>(this.url + "?where=inRoleProductOwner:eq:true",{ headers: this.getHeaders()})
      .pipe(
        catchError(this.handleError<UserAccount[]>(`get productOwners`)),
        map(content => this.deserializeArray(content))
      );
  }

  getDevelopers(): Observable<UserAccount[]> {
    return this.api.httpClient.get<UserAccount[]>(this.url + "?where=inRoleDeveloper:eq:true",{ headers: this.getHeaders()})
      .pipe(
        catchError(this.handleError<UserAccount[]>(`get developers`)),
        map(content => this.deserializeArray(content))
      );
  }
}
