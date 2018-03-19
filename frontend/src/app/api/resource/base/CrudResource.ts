import {BaseEntity} from '../../models/base/BaseEntity';
import {GetResource} from './GetResource';
import {Observable} from 'rxjs/Observable';
import {ApiService} from '../../Api';


export abstract class CrudResource<T extends BaseEntity<T>> extends GetResource<T> {

  constructor(entityName: string, api: ApiService) {
    super(entityName, api);
  }


  post (entity: T, xContent = this.api.xContent): Observable<T> {
    return this.api.httpClient.post<T>(this.url, entity, { headers: this.getHeaders(xContent)});
  }

  put (entity: T, xContent = this.api.xContent): Observable<T> {
    return this.api.httpClient.put<T>(this.url + "/" + entity.id, entity, { headers: this.getHeaders(xContent)});
  }

  patch (entity: T, xContent = this.api.xContent): Observable<T> {
    return this.api.httpClient.patch<T>(this.url + "/" + entity.id, entity, { headers: this.getHeaders(xContent)});
  }

  delete (id: string, xContent = this.api.xContent): Observable<T> {
    return this.api.httpClient.delete<T>(this.url + "/" + id, { headers: this.getHeaders(xContent)});
  }

  deleteToggle (id: string, xContent = this.api.xContent): Observable<T> {
    return this.api.httpClient.delete<T>(this.url + "/toggleDelete/" + id, { headers: this.getHeaders(xContent)});
  }

}
