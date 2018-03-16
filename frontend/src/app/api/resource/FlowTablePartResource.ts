import {CrudResource} from './base/CrudResource';
import {ApiService} from '../Api';
import {FlowTable} from '../models/FlowTable';
import {FlowTablePart} from '../models/FlowTablePart';

export class FlowTablePartResource extends CrudResource<FlowTablePart> {

  constructor(api: ApiService) {
    super("FlowTablePart", api);
  }

}
