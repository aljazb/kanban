import {CrudResource} from './base/CrudResource';
import {ApiService} from '../services/api.service';
import {Board} from '../models/Board';

export class BoardResource extends CrudResource<Board> {

  constructor(api: ApiService) {
    super("Board", api);
  }

}
