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

  static isKanbanMaster(m: Membership): boolean {
    return m != null && (
      m.memberType == MemberType.KANBAN_MASTER ||
      m.memberType == MemberType.DEVELOPER_AND_KANBAN_MASTER
    );
  }

  static isProductOwner(m: Membership): boolean {
    return m != null && (
      m.memberType == MemberType.PRODUCT_OWNER ||
      m.memberType == MemberType.DEVELOPER_AND_PRODUCT_OWNER
    );
  }

  static isDeveloper(m: Membership): boolean {
    return m != null &&(
      m.memberType == MemberType.DEVELOPER ||
      m.memberType == MemberType.DEVELOPER_AND_PRODUCT_OWNER ||
      m.memberType == MemberType.DEVELOPER_AND_KANBAN_MASTER
    );
  }

}
