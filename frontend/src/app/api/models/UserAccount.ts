import {BaseEntity} from './base/BaseEntity';
import {UserAccountMTMDevTeam} from './mtm/UserAccountMTMDevTeam';

export class UserAccount extends BaseEntity<UserAccount> {
  email: string;
  name: string;
  surname: string;

  joinedDevTeams: UserAccountMTMDevTeam;
}
