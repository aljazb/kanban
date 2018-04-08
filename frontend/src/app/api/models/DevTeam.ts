import {BaseEntity} from './base/BaseEntity';
import {Membership} from './Membership';
import {Project} from './Project';
import {Board} from './Board';
import {MemberType} from './enums/MemberType';
import {UserAccount} from './UserAccount';

export class DevTeam extends BaseEntity<DevTeam> {
  name: string;

  joinedUsers: Membership[];
  project: Project[];
  board: Board;

  static getDevelopersIds(dt: DevTeam): string[] {
    return this.getDevelopers(dt).map(dev => dev.id);
  }

  static getDevelopers(dt: DevTeam): UserAccount[] {
    return dt.joinedUsers.filter(mtm =>
      [MemberType.DEVELOPER, MemberType.DEVELOPER_AND_KANBAN_MASTER, MemberType.DEVELOPER_AND_PRODUCT_OWNER].includes(mtm.memberType))
      .map(mtm => mtm.userAccount);
  }

  static getKanbanMaster(dt: DevTeam): UserAccount {
    return dt.joinedUsers.filter(mtm => [MemberType.KANBAN_MASTER, MemberType.DEVELOPER_AND_KANBAN_MASTER]
      .includes(mtm.memberType)).map(mtm => mtm.userAccount)[0];
  }

  static getProductOwner(dt: DevTeam): UserAccount {
    return dt.joinedUsers.filter(mtm => [MemberType.PRODUCT_OWNER, MemberType.DEVELOPER_AND_PRODUCT_OWNER]
      .includes(mtm.memberType)).map(mtm => mtm.userAccount)[0];
  }
}
