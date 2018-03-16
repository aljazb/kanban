import {CrudResource} from './base/CrudResource';
import {ApiService} from '../Api';
import {FlowTable} from '../models/FlowTable';

export class FlowTableResource extends CrudResource<FlowTable> {

  constructor(api: ApiService) {
    super("FlowTable", api);
  }

}
