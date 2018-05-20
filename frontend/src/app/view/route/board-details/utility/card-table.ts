import {Card} from '../../../../api/models/Card';
import {BoardPart} from '../../../../api/models/BoardPart';
import {CardRepresentation} from './card-representation';

export class CardTable {
  currentBoardPart: BoardPart = null;

  leftBoardPart: BoardPart[] = null;
  rightBoardPart: BoardPart[] = null;

  moveBack: BoardPart[] = null;

  cards: CardRepresentation[];

  constructor(currentBoardPart: BoardPart, leftBoardPart: BoardPart[], rightBoardPart: BoardPart[], moveBack: BoardPart[]) {
    this.currentBoardPart = currentBoardPart;
    this.leftBoardPart = leftBoardPart;
    this.rightBoardPart = rightBoardPart;
    this.moveBack = moveBack;
    this.cards = [];
  }

  add(card: Card): void {
    this.cards.push(new CardRepresentation(card));
    this.cards.sort((a, b) => a.card.name.localeCompare(b.card.name));
  }

}
