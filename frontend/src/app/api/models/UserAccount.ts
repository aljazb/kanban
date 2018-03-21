import {BaseEntity} from './base/BaseEntity';
import {UserAccountMTMDevTeam} from './mtm/UserAccountMTMDevTeam';
import {Project} from './Project';

export class UserAccount extends BaseEntity<UserAccount> {
  email: string;
  firstName: string;
  lastName: string;
  joinedDevTeams: UserAccountMTMDevTeam[];
  projects: Project[];
  sentRequests: Request[];
  receivedRequests: Request[];
}
