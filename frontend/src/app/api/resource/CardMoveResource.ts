import {ApiService} from '../services/api.service';
import {CardMove} from '../models/card-move';
import {GetResource} from './base/GetResource';
import {Observable} from 'rxjs/Observable';
import {map} from 'rxjs/operators';
import {HttpResponse} from '@angular/common/http';

export class CardMoveResource extends GetResource<CardMove> {

  constructor(api: ApiService) {
    super("CardMove", api);
  }

  post (entity: CardMove, xContent = this.api.xContent): Observable<CardMove> {
    return this.api.httpClient.post<CardMove>(this.url, this.serialize(entity), { headers: this.getHeaders(xContent), observe: 'response'})
      .pipe(
        map((response: HttpResponse<CardMove>) => this.buildLocation(response)),
        map(content => this.deserialize(content))
      );
  }

}
