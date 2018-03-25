import {BaseEntity} from './base/BaseEntity';
import {DevTeam} from './DevTeam';
import {UserAccount} from './UserAccount';
import {BoardLane} from './BoardLane';

export class Project extends BaseEntity<Project> {
  name: string;
  description: string;
  productBuyer: string;

  startDate: Date;
  endDate: Date;

  boardLane: BoardLane;
  devTeam: DevTeam;
  owner: UserAccount;
}
