import {BaseEntity} from './base/BaseEntity';
import {Board} from './Board';
import {Card} from './Card';
import {Project} from './Project';

export class BoardLane extends BaseEntity<BoardLane> {
  name: string;

  project: Project;
  board: Board;
  cards: Card[];
}
