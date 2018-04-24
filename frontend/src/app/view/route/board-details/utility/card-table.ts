import {Card} from '../../../../api/models/Card';

export class CardTable {
  cards: Card[];

  constructor() {
    this.cards = [];
  }

  add(card: Card): void {
    this.cards.push(card);
    this.cards.sort((a, b) => a.name.localeCompare(b.name));
  }

}
