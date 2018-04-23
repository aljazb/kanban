import {CrudResource} from './base/CrudResource';
import {ApiService} from '../api.service';
import {CardMove} from '../models/card-move';

export class CardMoveResource extends CrudResource<CardMove> {

  constructor(api: ApiService) {
    super("CardMove", api);
  }

}
