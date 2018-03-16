import {CrudResource} from './base/CrudResource';
import {ApiService} from '../Api';
import {FlowTable} from '../models/FlowTable';
import {FlowTablePart} from '../models/FlowTablePart';
import {Card} from '../models/Card';

export class CardResource extends CrudResource<Card> {

  constructor(api: ApiService) {
    super("Card", api);
  }

}
