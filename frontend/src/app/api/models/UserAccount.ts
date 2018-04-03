import {BaseEntity} from './base/BaseEntity';
import {UserAccountMTMDevTeam} from './mtm/UserAccountMTMDevTeam';
import {Project} from './Project';

export class UserAccount extends BaseEntity<UserAccount>{

  username: string;
  email: string;
  firstName: string;
  lastName: string;

  password: string; // Transient field, used only for creating user with initial password

  inRoleKanbanMaster: boolean;
  inRoleAdministrator: boolean;
  inRoleDeveloper: boolean;
  inRoleProductOwner: boolean;

  joinedDevTeams: UserAccountMTMDevTeam[];
  projects: Project[];
  sentRequests: Request[];
  receivedRequests: Request[];

}
