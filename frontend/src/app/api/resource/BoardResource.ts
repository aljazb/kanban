import {CrudResource} from './base/CrudResource';
import {ApiService} from '../api.service';
import {BoardPart} from '../models/BoardPart';
import {Board} from '../models/Board';

export class BoardResource extends CrudResource<Board> {

  constructor(api: ApiService) {
    super("Board", api);
  }

}