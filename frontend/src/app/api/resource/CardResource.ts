import {CrudResource} from './base/CrudResource';
import {ApiService} from '../services/api.service';
import {Card} from '../models/Card';
import {map} from 'rxjs/operators';
import {Observable} from 'rxjs/Observable';

export class CardResource extends CrudResource<Card> {

  constructor(api: ApiService) {
    super("Card", api);
  }

  delete (id: string, xContent = this.api.xContent): Observable<Card> {
    return null;
  }

  deleteWithMessage (id: string, deleteMessage: string, xContent = this.api.xContent): Observable<Card> {
    return this.api.httpClient.delete<Card>(this.url + "/" + id, { headers: this.getHeaders(xContent), params: { message: deleteMessage }})
      .pipe(map(content => this.deserialize(content)));
  }

}
