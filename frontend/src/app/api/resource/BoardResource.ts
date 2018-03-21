import {CrudResource} from './base/CrudResource';
import {ApiService} from '../Api';
import {Board} from '../models/Board';
import {BoardPart} from '../models/BoardPart';

export class BoardResource extends CrudResource<BoardPart> {

  constructor(api: ApiService) {
    super("BoardResource", api);
  }

}
