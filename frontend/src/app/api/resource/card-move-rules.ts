import {BoardPart} from '../models/BoardPart';
import {Board} from '../models/Board';
import {BaseEntity} from '../models/base/BaseEntity';

export class CardMoveRule extends BaseEntity<CardMoveRule> {

  roleKanbanMasterAllowed: boolean;
  roleAdministratorAllowed: boolean;
  roleDeveloperAllowed: boolean;
  roleProductOwnerAllowed: boolean;

  canReject: boolean;

  from: BoardPart;
  to: BoardPart;

  board: Board;


}
