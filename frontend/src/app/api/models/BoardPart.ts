import {BaseEntity} from './base/BaseEntity';
import {Board} from './Board';
import {Card} from './Card';
import {isNullOrUndefined} from 'util';

export class BoardPart extends BaseEntity<BoardPart> {
  name: string;
  maxWip: number;
  currentWip: number;

  orderIndex: number;
  leaf: boolean;

  board: Board;

  parent: BoardPart;
  children: BoardPart[];

  cards: Card[];

}
