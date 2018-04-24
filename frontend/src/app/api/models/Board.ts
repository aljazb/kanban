import {BaseEntity} from './base/BaseEntity';
import {BoardPart} from './BoardPart';
import {Project} from './Project';
import {UserAccount} from './UserAccount';
import {Membership} from './Membership';

export class Board extends BaseEntity<Board> {
  name: string;

  highestPriority: string;
  startDev: string;
  endDev: string;
  acceptanceTesting: string;

  owner: UserAccount;
  boardParts: BoardPart[];
  projects: Project[];
  membership: Membership;

  public static getLeafParts(boardParts: BoardPart[]): BoardPart[] {
    let array = [];
    boardParts.sort((a, b) => a.orderIndex - b.orderIndex);

    boardParts.forEach(boardPart => {
      if (boardPart.leaf) {
        array.push(boardPart);
      } else {
        let cArray = this.getLeafParts(boardPart.children);
        array = array.concat(cArray);
      }
    });
    return array;
  }
}
