import {Card} from '../../../../api/models/Card';
import {BoardPart} from '../../../../api/models/BoardPart';

export class CardTable {
  leftBoardPart: BoardPart = null;
  rightBoardPart: BoardPart = null;

  cards: Card[];

  constructor(leftBoardPart: BoardPart, rightBoardPart: BoardPart) {
    this.leftBoardPart = leftBoardPart;
    this.rightBoardPart = rightBoardPart;
    this.cards = [];
  }

  add(card: Card): void {
    this.cards.push(card);
    this.cards.sort((a, b) => a.name.localeCompare(b.name));
  }

}
