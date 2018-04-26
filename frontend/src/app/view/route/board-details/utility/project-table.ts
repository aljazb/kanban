import {CardTable} from './card-table';
import {Card} from '../../../../api/models/Card';
import {BoardPart} from '../../../../api/models/BoardPart';
import {Project} from '../../../../api/models/Project';
import {Membership} from '../../../../api/models/Membership';

export class ProjectTable {
  project: Project;
  cardTables: CardTable[];

  constructor(project: Project, rows: number, leafBoardParts: BoardPart[]) {
    this.project = project;
    this.cardTables = [];

     let moveBackBoardParts = leafBoardParts.filter(value => value.leafNumber <= project.board.highestPriority);

    for(let i=0; i<rows; i++) {
      let leftBp: BoardPart = null;
      let rightBp: BoardPart = null;
      let moveBack: BoardPart[] = null;

      let isAllowedToMoveCardsLeft = true;
      let isAllowedToMoveCardsRight = true;
      let isAllowedToMoveCards = false;
      let isAllowedMoveBack = false;

      if(project.membership != null) {
        let isDeveloper = Membership.isDeveloper(project.membership);
        let isProductOwner = Membership.isProductOwner(project.membership);
        let isKanbanMaster = Membership.isKanbanMaster(project.membership);

        if(i <= project.board.highestPriority) {

          isAllowedToMoveCards = isProductOwner || isKanbanMaster;
          if(i == project.board.highestPriority && isKanbanMaster) isAllowedToMoveCardsRight = false;

        } else if(project.board.startDev <= i && i <= project.board.endDev) {

          isAllowedToMoveCards = isDeveloper || isKanbanMaster;
          if(i == project.board.startDev) isAllowedToMoveCardsLeft = false;
          if(i == project.board.endDev) isAllowedToMoveCardsRight = isKanbanMaster;

        } else if(project.board.acceptanceTesting <= i) {

          isAllowedToMoveCards = isProductOwner;
          if(i == project.board.acceptanceTesting && isProductOwner) {
            isAllowedToMoveCardsLeft = false;
            isAllowedMoveBack = true;
          }
        }

      }

      if(isAllowedToMoveCards) {
        if(isAllowedToMoveCardsLeft && i != 0) {
          leftBp = leafBoardParts[i - 1];
        }
        if(isAllowedToMoveCardsRight && i + 1 < leafBoardParts.length) {
          rightBp = leafBoardParts[i + 1];
        }
      }

      if(isAllowedMoveBack) {
        moveBack = moveBackBoardParts;
      }

      this.cardTables.push(new CardTable(leftBp, rightBp, moveBack));
    }
  }

  add(card: Card, index: number): void {
    this.cardTables[index].add(card);
  }

}
