import {BaseEntity} from './base/BaseEntity';
import {BoardPart} from './BoardPart';
import {Project} from './Project';
import {UserAccount} from './UserAccount';

export class Board extends BaseEntity<Board> {
  name: string;
  highestPriority: number;
  startDev: number;
  endDev: number;
  acceptanceTesting: number;

  owner: UserAccount;
  boardParts: BoardPart[];
  projects: Project[];
}
