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
    let c = new Card();
    c.deleteMessage = deleteMessage;

    return this.api.httpClient.put<Card>(this.url + "/delete/" + id, c, { headers: this.getHeaders(xContent)})
      .pipe(map(content => this.deserialize(content)));
  }

}
