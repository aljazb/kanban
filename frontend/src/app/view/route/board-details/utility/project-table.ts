import {CardTable} from './card-table';
import {Card} from '../../../../api/models/Card';

export class ProjectTable {
  id: string;
  name: string;
  cardTables: CardTable[];

  constructor(id: string, name: string, rows: number) {
    this.id = id;
    this.name = name;
    this.cardTables = [];

    for(let i=0; i<rows; i++) {
      this.cardTables.push(new CardTable());
    }
  }

  add(card: Card, index: number): void {
    this.cardTables[index].add(card);
  }

}
