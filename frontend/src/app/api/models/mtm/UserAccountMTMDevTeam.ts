import {BaseEntity} from '../base/BaseEntity';
import {UserAccount} from '../UserAccount';
import {DevTeam} from '../DevTeam';

export class UserAccountMTMDevTeam extends BaseEntity<UserAccountMTMDevTeam> {
  memberType: string; // Member Type
  userAccount: UserAccount;
  devTeam: DevTeam;
}
