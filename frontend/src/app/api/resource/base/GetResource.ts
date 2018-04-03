import {BaseEntity} from '../../models/base/BaseEntity';
import {BaseResource} from './BaseResource';
import {Observable} from 'rxjs/Observable';
import {ApiService} from '../../api.service';
import {HttpParams, HttpResponse} from '@angular/common/http';
import {catchError, map} from 'rxjs/operators';
import {Paging} from '../../dto/Paging';

export abstract class GetResource<T extends BaseEntity<T>> extends BaseResource<T> {

  constructor(entityName: string, api: ApiService) {
    super(entityName, api);
  }

  private buildPaging(resp: HttpResponse<T[]>): Paging<T> {
    return new Paging<T>(+resp.headers.get('X-Count'), this.serializeArray(resp.body));
  }

  getList (httpParams: HttpParams = null): Observable<Paging<T>> {
    return this.api.httpClient.get<T[]>(this.url, { headers: this.getHeaders(), params: httpParams, observe: 'response'})
      .pipe(
        catchError(this.handleError<T[]>(`getList`)),
        map((resp: HttpResponse<T[]>) => this.buildPaging(resp))
      );
  }

  get (id: string): Observable<T> {
    return this.api.httpClient.get<T>(this.url + "/" + id, { headers: this.getHeaders()})
      .pipe(
        catchError(this.handleError<T>(`get`)),
        map(content => this.serialize(content))
      );
  }

}
