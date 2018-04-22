import {BaseEntity} from './base/BaseEntity';
import {BoardPart} from './BoardPart';
import {Project} from './Project';
import {UserAccount} from './UserAccount';
import {Membership} from './Membership';

export class Board extends BaseEntity<Board> {
  name: string;

  highestPriority: string;
  startDev: string;
  endDev: string;
  acceptanceTesting: string;

  owner: UserAccount;
  boardParts: BoardPart[];
  projects: Project[];
  membership: Membership;
}
