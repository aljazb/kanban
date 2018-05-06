import {Card} from '../../../../api/models/Card';
import {BoardPart} from '../../../../api/models/BoardPart';

export class CardTable {
  currentBoardPart: BoardPart = null;
  leftBoardPart: BoardPart = null;
  rightBoardPart: BoardPart = null;

  moveBack: BoardPart[] = null;

  cards: Card[];

  constructor(currentBoardPart: BoardPart, leftBoardPart: BoardPart, rightBoardPart: BoardPart, moveBack: BoardPart[]) {
    this.currentBoardPart = currentBoardPart;
    this.leftBoardPart = leftBoardPart;
    this.rightBoardPart = rightBoardPart;
    this.moveBack = moveBack;
    this.cards = [];
  }

  add(card: Card): void {
    this.cards.push(card);
    this.cards.sort((a, b) => a.name.localeCompare(b.name));
  }

}
