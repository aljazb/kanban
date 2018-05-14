import {BaseEntity} from './base/BaseEntity';
import {Project} from './Project';
import {BoardPart} from './BoardPart';
import {CardMove} from './card-move';
import {SubTask} from './sub-task';
import {Membership} from './Membership';
import {CardType} from './enums/card-type';
import {UserAccount} from './UserAccount';

export class Card extends BaseEntity<Card> {
  name: string;
  code: string;
  cardType: CardType;
  description: string;
  workload: number;

  color: string;

  assignedTo: UserAccount;
  project: Project;
  boardPart: BoardPart;

  silverBullet: boolean;
  rejected: boolean;

  cardMoves: CardMove[];
  subTasks: SubTask[];
  membership: Membership;
}
