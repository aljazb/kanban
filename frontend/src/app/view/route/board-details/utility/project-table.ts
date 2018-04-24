import {CardTable} from './card-table';
import {Card} from '../../../../api/models/Card';
import {BoardPart} from '../../../../api/models/BoardPart';

export class ProjectTable {
  id: string;
  name: string;
  cardTables: CardTable[];

  constructor(id: string, name: string, rows: number, leafBoardParts: BoardPart[]) {
    this.id = id;
    this.name = name;
    this.cardTables = [];

    for(let i=0; i<rows; i++) {
      let leftBp: BoardPart = null;
      if(i != 0) leftBp = leafBoardParts[i - 1];

      let rightBp: BoardPart = null;
      if(i + 1 < leafBoardParts.length) rightBp = leafBoardParts[i + 1];

      this.cardTables.push(new CardTable(leftBp, rightBp));
    }
  }

  add(card: Card, index: number): void {
    this.cardTables[index].add(card);
  }

}
