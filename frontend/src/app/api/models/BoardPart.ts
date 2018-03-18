import {BaseEntity} from './base/BaseEntity';
import {Board} from './Board';
import {Card} from './Card';

export class BoardPart extends BaseEntity<BoardPart> {
  name: string;
  maxWip: number;
  isLeaf: boolean;

  board: Board;

  parent: BoardPart;
  children: BoardPart[];

  cards: Card[];
}
