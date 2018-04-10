import {CardTable} from './card-table';
import {Card} from '../../../../api/models/Card';

export class ProjectTable {
  id: string;
  name: string;
  cards: CardTable[];

  constructor(id: string, name: string) {
    this.id = id;
    this.name = name;
  }

  add(card: Card, index: number): void {
    this.cards[index].add(card);
  }

}
