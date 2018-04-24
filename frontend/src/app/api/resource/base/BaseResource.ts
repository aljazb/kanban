import {BaseEntity} from '../../models/base/BaseEntity';
import {HttpHeaders, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {isNullOrUndefined} from 'util';
import {ApiService} from '../../services/api.service';

export abstract class BaseResource<T extends BaseEntity<T>> {

  protected api: ApiService;
  protected url: string;

  constructor(entityName: string, api: ApiService) {
    this.api = api;
    this.url = api.url + "/" + entityName;
  }

  getHeaders(xContent=false) : HttpHeaders {

    let headers = new HttpHeaders();
    headers = headers.append('Content-Type', 'application/json');
    headers = headers.append('Accept', 'application/json');

    if(xContent){
      headers = headers.append('X-Content', 'true');
    }

    return headers;
  }

  protected handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      console.log(`${operation} failed: ${error.message}`);
      console.error(error);

      return of(result as T);
    };
  }

  protected deserialize<E extends BaseEntity<E>>(content: any): E {
    let obj = <E> this.api.jsog.deserialize(content);
    this.deleteIds(obj);
    return obj;
  }

  protected deserializeArray<E extends BaseEntity<E>>(content: any): E[] {
    let obj = <E[]> this.api.jsog.deserialize(content);
    this.deleteIds(obj);
    return obj;
  }

  protected deleteIds(obj: any) {
    if (isNullOrUndefined(obj)) {
      return;
    }
    if (!Array.isArray(obj)) {
      if (isNullOrUndefined(obj["@id"])) {
        return;
      }
      delete obj["@id"];
    }
    for (let key in obj) {
      this.deleteIds(obj[key]);
    }
  }

  protected serialize(entity) {
    return this.api.jsog.serialize(entity);
  }

  protected buildLocation(resp: HttpResponse<T>): T {
    let entity = resp.body;
    if(entity == null) {
      let locationId = resp.headers.get('location');
      if(locationId) {
        locationId = locationId.substr(locationId.indexOf("/") + 1);

        entity = {} as T;
        entity.id = locationId;
      }
    }
    return entity;
  }

}
