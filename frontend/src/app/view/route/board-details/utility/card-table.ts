import {Card} from '../../../../api/models/Card';

export class CardTable {
  id: string;
  cards: Card[];

  constructor(id: string) {
    this.id = id;
    this.cards = [];
  }

  add(card: Card): void {
    this.cards.push(card);
  }

}
