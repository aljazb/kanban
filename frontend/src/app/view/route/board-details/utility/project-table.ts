import {CardTable} from './card-table';
import {Card} from '../../../../api/models/Card';
import {BoardPart} from '../../../../api/models/BoardPart';
import {Project} from '../../../../api/models/Project';
import {Membership} from '../../../../api/models/Membership';
import {CardMoveRule} from '../../../../api/resource/card-move-rules';

export class ProjectTable {
  project: Project;
  cardTables: CardTable[];

  constructor(project: Project, rows: number, leafBoardParts: BoardPart[], cardMoveRulesMap: Map<string, CardMoveRule[]>) {
    this.project = project;
    this.cardTables = [];

     let moveBackBoardParts = leafBoardParts.filter(value => value.leafNumber <= project.board.highestPriority);

    for(let i=0; i<rows; i++) {
      let currentBp: BoardPart = leafBoardParts[i];
      let leftBp: BoardPart[] = [];
      let rightBp: BoardPart[] = [];
      let moveBack: BoardPart[] = null;


      if(project.membership != null) {
        let isDeveloper = Membership.isDeveloper(project.membership);
        let isProductOwner = Membership.isProductOwner(project.membership);
        let isKanbanMaster = Membership.isKanbanMaster(project.membership);

        let cardMoveRules = cardMoveRulesMap.get(currentBp.id);

        if(cardMoveRules) {
          cardMoveRules.forEach(cmr => {

            if(
              (cmr.roleKanbanMasterAllowed && isKanbanMaster) ||
              (cmr.roleProductOwnerAllowed && isProductOwner) ||
              (cmr.roleDeveloperAllowed && isDeveloper)
            ) {
              if(cmr.canReject) {
                moveBack = moveBackBoardParts;
              } else {
                let to: BoardPart = cmr.to;
                if(to.leafNumber == currentBp.leafNumber) {
                  to = cmr.from;
                }

                if(to.leafNumber > currentBp.leafNumber) {
                  rightBp.push(to);
                } else {
                  leftBp.push(to);
                }
              }
            }
          });
        }
      }

      this.cardTables.push(new CardTable(currentBp, leftBp, rightBp, moveBack));
    }
  }

  add(card: Card, index: number): void {
    this.cardTables[index].add(card);
  }

}
