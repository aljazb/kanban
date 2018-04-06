import {BaseEntity} from '../../models/base/BaseEntity';
import {GetResource} from './GetResource';
import {Observable} from 'rxjs/Observable';
import {ApiService} from '../../api.service';
import {catchError, map} from 'rxjs/operators';


export abstract class CrudResource<T extends BaseEntity<T>> extends GetResource<T> {

  constructor(entityName: string, api: ApiService) {
    super(entityName, api);
  }


  post (entity: T, xContent = this.api.xContent): Observable<T> {
    return this.api.httpClient.post<T>(this.url, this.serialize(entity), { headers: this.getHeaders(xContent)})
      .pipe(
        catchError(this.handleError<T>(`post`)),
        map(content => this.deserialize(content))
      );
  }

  put (entity: T, xContent = this.api.xContent): Observable<T> {
    return this.api.httpClient.put<T>(this.url + "/" + entity.id, this.serialize(entity), { headers: this.getHeaders(xContent)})
      .pipe(
        catchError(this.handleError<T>(`put`)),
        map(content => this.deserialize(content))
      );
  }

  patch (entity: T, xContent = this.api.xContent): Observable<T> {
    return this.api.httpClient.patch<T>(this.url + "/" + entity.id, this.serialize(entity), { headers: this.getHeaders(xContent)})
      .pipe(
        catchError(this.handleError<T>(`patch`)),
        map(content => this.deserialize(content))
      );
  }

  delete (id: string, xContent = this.api.xContent): Observable<T> {
    return this.api.httpClient.delete<T>(this.url + "/" + id, { headers: this.getHeaders(xContent)})
      .pipe(
        catchError(this.handleError<T>(`delete`)),
        map(content => this.deserialize(content))
      );
  }

  changeStatus (id: string, xContent = this.api.xContent): Observable<T> {
    return this.api.httpClient.put<T>(this.url + "/" + id + "/status/", null,{ headers: this.getHeaders(xContent)})
      .pipe(
        catchError(this.handleError<T>(`status`)),
        map(content => this.deserialize(content))
      );
  }

}
