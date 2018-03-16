import {BaseEntity} from '../../models/base/BaseEntity';
import {ApiService} from '../../Api';
import {HttpHeaders} from '@angular/common/http';

export abstract class BaseResource<T extends BaseEntity<T>> {

  protected api: ApiService;
  protected url: string;

  constructor(entityName: string, api: ApiService) {
    this.api = api;
    this.url = api.url + "/" + entityName;
  }

  static getHeaders(xContent=false) : HttpHeaders {

    let headers = new HttpHeaders();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');

    if(xContent){
      headers.append('X-Content', 'application/json');
    }

    return headers;
  }


}
