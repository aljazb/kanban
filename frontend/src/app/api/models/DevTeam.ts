import {BaseEntity} from './base/BaseEntity';
import {UserAccountMTMDevTeam} from './mtm/UserAccountMTMDevTeam';
import {Project} from './Project';
import {FlowTable} from './FlowTable';

export class DevTeam extends BaseEntity<DevTeam> {
  name: string;

  joinedUsers: UserAccountMTMDevTeam[];
  project: Project[];
  flowTable: FlowTable;

}
