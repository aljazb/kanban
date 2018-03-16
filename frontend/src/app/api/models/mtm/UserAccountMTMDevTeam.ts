import {BaseEntity} from '../base/BaseEntity';
import {MemberType} from '../enums/MemberType';
import {UserAccount} from '../UserAccount';
import {DevTeam} from '../DevTeam';

export class UserAccountMTMDevTeam extends BaseEntity<UserAccountMTMDevTeam> {
  memberType: MemberType;
  userAccount: UserAccount;
  devTeam: DevTeam;
}
