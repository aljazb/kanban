import {UserAccount} from '../models/UserAccount';
import {Observable} from 'rxjs/Observable';
import {ApiService} from '../api.service';
import {catchError} from 'rxjs/operators';
import {GetResource} from './base/GetResource';

export class UserAccountResource extends GetResource<UserAccount> {

  constructor(api: ApiService) {
    super("UserAccount", api);
  }

  login (): Observable<UserAccount> {
    return this.api.httpClient.get<UserAccount>(this.url + "/login", { headers: this.getHeaders()})
      .pipe(catchError(this.handleError<UserAccount>(`login`)));
  }

  post (entity: UserAccount, xContent = this.api.xContent): Observable<UserAccount> {
    console.log(entity);
    console.log(this.url);
    return this.api.httpClient.post<UserAccount>(this.url, entity, { headers: this.getHeaders(xContent)})
      .pipe(catchError(this.handleError<UserAccount>(`post`)));
  }

  put (entity: UserAccount, xContent = this.api.xContent): Observable<UserAccount> {
    console.log(entity);
    return this.api.httpClient.put<UserAccount>(this.url + "/" + entity.id, entity, { headers: this.getHeaders(xContent)})
      .pipe(catchError(this.handleError<UserAccount>(`patch`)));
  }

  delete (id: string, xContent = this.api.xContent): Observable<UserAccount> {
    console.log(id);
    return this.api.httpClient.delete<UserAccount>(this.url + "/" + id, { headers: this.getHeaders(xContent)})
      .pipe(catchError(this.handleError<UserAccount>(`delete`)));
  }

  changeStatus (id: string, xContent = this.api.xContent): Observable<UserAccount> {
    console.log(id);
    return this.api.httpClient.put<UserAccount>(this.url + "/" + id + "/status/", null,{ headers: this.getHeaders(xContent)})
      .pipe(catchError(this.handleError<UserAccount>(`status`)));
  }

  getKanbanMasters(): Observable<UserAccount[]> {
    return this.api.httpClient.get<UserAccount[]>(this.url + "?where=inRoleKanbanMaster:eq:true",{ headers: this.getHeaders()})
      .pipe(catchError(this.handleError<UserAccount[]>(`get kanbanMasters`)));
  }

  getProductOwners(): Observable<UserAccount[]> {
    return this.api.httpClient.get<UserAccount[]>(this.url + "?where=inRoleProductOwner:eq:true",{ headers: this.getHeaders()})
      .pipe(catchError(this.handleError<UserAccount[]>(`get productOwners`)));
  }

  getDevelopers(): Observable<UserAccount[]> {
    return this.api.httpClient.get<UserAccount[]>(this.url + "?where=inRoleDeveloper:eq:true",{ headers: this.getHeaders()})
      .pipe(catchError(this.handleError<UserAccount[]>(`get developers`)));
  }
}
