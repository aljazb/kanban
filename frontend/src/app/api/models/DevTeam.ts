import {BaseEntity} from './base/BaseEntity';
import {UserAccountMTMDevTeam} from './mtm/UserAccountMTMDevTeam';
import {Project} from './Project';
import {Board} from './Board';

export class DevTeam extends BaseEntity<DevTeam> {
  name: string;

  joinedUsers: UserAccountMTMDevTeam[];
  project: Project[];
  board: Board;

}
