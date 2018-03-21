import {BaseEntity} from '../../models/base/BaseEntity';
import {ApiService} from '../../Api';
import {HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';

export abstract class BaseResource<T extends BaseEntity<T>> {

  protected api: ApiService;
  protected url: string;

  constructor(entityName: string, api: ApiService) {
    this.api = api;
    this.url = api.url + "/" + entityName;
  }

  getHeaders(xContent=false) : HttpHeaders {

    let headers = new HttpHeaders();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');

    if(xContent){
      headers.append('X-Content', 'application/json');
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

}
