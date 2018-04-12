import {BaseEntity} from './base/BaseEntity';
import {UserAccount} from './UserAccount';
import {DevTeam} from './DevTeam';
import {MemberType} from './enums/MemberType';

export class Membership extends BaseEntity<Membership> {
  memberType: string; // Member Type
  userAccount: UserAccount;
  devTeam: DevTeam;

  constructor(memberType: string=MemberType.DEVELOPER, userAccount: UserAccount=null) {
    super();
    this.memberType = memberType;
    this.userAccount = userAccount;
  }

}
