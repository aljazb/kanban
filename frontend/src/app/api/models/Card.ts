import {BaseEntity} from './base/BaseEntity';
import {Project} from './Project';
import {BoardPart} from './BoardPart';
import {BoardLane} from './BoardLane';

export class Card extends BaseEntity<Card> {
  name: string;
  description: string;
  workload: number;

  project: Project;

  boardPart: BoardPart;
  boardLane: BoardLane;
}
