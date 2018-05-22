import {BaseEntity} from './base/BaseEntity';
import {BoardPart} from './BoardPart';
import {Project} from './Project';
import {UserAccount} from './UserAccount';
import {Membership} from './Membership';
import {CardMoveRule} from '../resource/card-move-rules';

export class Board extends BaseEntity<Board> {
  name: string;

  highestPriority: number;
  startDev: number;
  endDev: number;
  acceptanceTesting: number;

  showCardType: boolean;
  showWorkload: boolean;
  showDueDate: boolean;

  remainingDays: number;

  owner: UserAccount;
  boardParts: BoardPart[];
  projects: Project[];
  membership: Membership;

  cardMoveRules: CardMoveRule[];


  public static getLeafParts(boardParts: BoardPart[], leafBoardParts: BoardPart[]=[]): BoardPart[] {
    if(boardParts != null){
      boardParts.sort((a, b) => a.orderIndex - b.orderIndex);

      boardParts.forEach(boardPart => {
        if (BoardPart.hasChildren(boardPart)) {
          this.getLeafParts(boardPart.children, leafBoardParts);
        } else {
          leafBoardParts.push(boardPart);
        }
      });
    }

    return leafBoardParts;
  }

  public static sortBoardParts(boardParts: BoardPart[]): void {
    boardParts.forEach(boardPart => {
      if(boardPart.children) {
        this.sortBoardParts(boardPart.children);
      }
    });
    boardParts.sort((a, b) => a.orderIndex - b.orderIndex);
  }
}
