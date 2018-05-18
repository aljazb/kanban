import {BaseEntity} from '../../models/base/BaseEntity';
import {GetResource} from './GetResource';
import {Observable} from 'rxjs/Observable';
import {map} from 'rxjs/operators';
import {HttpResponse} from '@angular/common/http';
import {ApiService} from '../../services/api.service';


export abstract class CrudResource<T extends BaseEntity<T>> extends GetResource<T> {

  constructor(entityName: string, api: ApiService) {
    super(entityName, api);
  }

  post (entity: T, xContent = this.api.xContent): Observable<T> {
    return this.api.httpClient.post<T>(this.url, this.serialize(entity), { headers: this.getHeaders(xContent), observe: 'response'})
      .pipe(
        map((response: HttpResponse<T>) => this.buildLocation(response)),
        map(content => this.deserialize(content))
      );
  }

  put (entity: T, xContent = this.api.xContent, reason: string = null): Observable<T> {
    return this.api.httpClient.put<T>(this.url + "/" + entity.id, this.serialize(entity), { headers: this.getHeaders(xContent), params: { reason: reason }})
      .pipe(map(content => this.deserialize(content)));
  }

  patch (entity: T, xContent = this.api.xContent): Observable<T> {
    return this.api.httpClient.patch<T>(this.url + "/" + entity.id, this.serialize(entity), { headers: this.getHeaders(xContent)})
      .pipe(map(content => this.deserialize(content)));
  }

  delete (id: string, xContent = this.api.xContent): Observable<T> {
    return this.api.httpClient.delete<T>(this.url + "/" + id, { headers: this.getHeaders(xContent)})
      .pipe(map(content => this.deserialize(content)));
  }

  changeStatus (id: string, xContent = this.api.xContent): Observable<T> {
    return this.api.httpClient.put<T>(this.url + "/" + id + "/status/", null,{ headers: this.getHeaders(xContent)})
      .pipe(map(content => this.deserialize(content)));
  }

}
