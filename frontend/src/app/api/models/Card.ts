import {BaseEntity} from './base/BaseEntity';
import {Project} from './Project';
import {BoardPart} from './BoardPart';
import {CardMove} from './card-move';
import {SubTask} from './sub-task';
import {Membership} from './Membership';

export class Card extends BaseEntity<Card> {
  name: string;
  description: string;
  workload: number;

  color: string;

  project: Project;
  boardPart: BoardPart;

  cardMoves: CardMove[];
  subTasks: SubTask[];
  membership: Membership;
}
