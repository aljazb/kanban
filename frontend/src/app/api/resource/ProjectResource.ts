import {CrudResource} from './base/CrudResource';
import {ApiService} from '../services/api.service';
import {Project} from '../models/Project';

export class ProjectResource extends CrudResource<Project> {

  constructor(api: ApiService) {
    super("Project", api);
  }

}
