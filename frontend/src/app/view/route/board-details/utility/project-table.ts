import {CardTable} from './card-table';
import {Card} from '../../../../api/models/Card';
import {BoardPart} from '../../../../api/models/BoardPart';
import {Project} from '../../../../api/models/Project';
import {UserAccount} from '../../../../api/models/UserAccount';
import {Membership} from '../../../../api/models/Membership';

export class ProjectTable {
  project: Project;
  cardTables: CardTable[];

  constructor(project: Project, rows: number, leafBoardParts: BoardPart[]) {
    this.project = project;
    this.cardTables = [];

    let isAllowedToMoveCards = project.membership != null;

    for(let i=0; i<rows; i++) {
      let leftBp: BoardPart = null;
      let rightBp: BoardPart = null;

      if(isAllowedToMoveCards) {
        if(i != 0) leftBp = leafBoardParts[i - 1];
        if(i + 1 < leafBoardParts.length) rightBp = leafBoardParts[i + 1];
      }

      this.cardTables.push(new CardTable(leftBp, rightBp));
    }
  }

  add(card: Card, index: number): void {
    this.cardTables[index].add(card);
  }

}
