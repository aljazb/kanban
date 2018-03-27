import {CrudResource} from './base/CrudResource';
import {ApiService} from '../api.service';
import {Board} from '../models/Board';
import {BoardPart} from '../models/BoardPart';
import {Card} from '../models/Card';

export class CardResource extends CrudResource<Card> {

  constructor(api: ApiService) {
    super("Card", api);
  }

}
