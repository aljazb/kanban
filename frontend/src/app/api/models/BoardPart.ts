import {BaseEntity} from './base/BaseEntity';
import {Board} from './Board';
import {Card} from './Card';
import {isNullOrUndefined} from 'util';
import {CardMoveRule} from '../resource/card-move-rules';

export class BoardPart extends BaseEntity<BoardPart> {
  name: string;
  maxWip: number;
  currentWip: number;

  orderIndex: number;
  leafNumber: number;

  board: Board;

  parent: BoardPart;
  children: BoardPart[];

  cards: Card[];

  cardMovesRulesFrom: CardMoveRule;
  cardMovesRulesTo: CardMoveRule;

  static hasChildren(boardPart: BoardPart): boolean {
    return boardPart.children != null && boardPart.children.length > 0;
  }

}
