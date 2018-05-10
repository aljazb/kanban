import {BaseEntity} from './base/BaseEntity';
import {DevTeam} from './DevTeam';
import {UserAccount} from './UserAccount';
import {Card} from './Card';
import {Board} from './Board';
import {Membership} from './Membership';

export class Project extends BaseEntity<Project> {
  code: string;
  name: string;
  description: string;
  productBuyer: string;

  startDate: number;
  endDate: number;

  owner: UserAccount;
  devTeam: DevTeam;
  cards: Card[];
  board: Board;

  membership: Membership;
  firstColumnFull: boolean;
  silverBulletInHighestPriority: boolean;
}
