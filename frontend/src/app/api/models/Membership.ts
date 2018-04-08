import {BaseEntity} from './base/BaseEntity';
import {UserAccount} from './UserAccount';
import {DevTeam} from './DevTeam';

export class Membership extends BaseEntity<Membership> {
  memberType: string; // Member Type
  userAccount: UserAccount;
  devTeam: DevTeam;
}
