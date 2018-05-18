import {ApiService} from '../services/api.service';
import {CrudResource} from './base/CrudResource';
import {SubTask} from '../models/sub-task';

export class SubTaskResource extends CrudResource<SubTask> {

  constructor(api: ApiService) {
    super("SubTask", api);
  }

}
