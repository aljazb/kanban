import {CrudResource} from './base/CrudResource';
import {ApiService} from '../Api';
import {DevTeam} from '../models/DevTeam';

export class DevTeamResource extends CrudResource<DevTeam> {

  constructor(api: ApiService) {
    super("DevTeam", api);
  }

}
