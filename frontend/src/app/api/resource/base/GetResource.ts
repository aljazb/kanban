import {BaseEntity} from '../../models/base/BaseEntity';
import {BaseResource} from './BaseResource';
import {Observable} from 'rxjs/Observable';


export abstract class GetResource<T extends BaseEntity<T>> extends BaseResource<T> {

  getList (): Observable<T[]> {
    return this.api.httpClient.get<T[]>(this.url, { headers: this.getHeaders()});
  }

  get (id: string): Observable<T> {
    return this.api.httpClient.get<T>(this.url + "/" + id, { headers: this.getHeaders()});
  }

}
