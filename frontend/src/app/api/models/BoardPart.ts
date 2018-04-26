import {BaseEntity} from './base/BaseEntity';
import {Board} from './Board';
import {Card} from './Card';
import {isNullOrUndefined} from 'util';

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

  static hasChildren(boardPart: BoardPart): boolean {
    return boardPart.children != null && boardPart.children.length > 0;
  }

}
