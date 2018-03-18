import {BaseEntity} from '../../models/base/BaseEntity';
import {BaseResource} from './BaseResource';
import {Observable} from 'rxjs/Observable';
import {ApiService} from '../../Api';


export abstract class GetResource<T extends BaseEntity<T>> extends BaseResource<T> {

  constructor(entityName: string, api: ApiService) {
    super(entityName, api);
  }

  getList (): Observable<T[]> {
    return this.api.httpClient.get<T[]>(this.url, { headers: this.getHeaders()});
  }

  get (id: string): Observable<T> {
    return this.api.httpClient.get<T>(this.url + "/" + id, { headers: this.getHeaders()});
  }

}
