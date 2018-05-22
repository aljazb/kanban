import {Card} from '../../../../api/models/Card';

export class CardRepresentation{

  card: Card;

  showLeft: boolean = false;
  showRight: boolean = false;


  constructor(card: Card) {
    this.card = card;
  }

  hideRight(): void {
    setTimeout(() => {this.showRight = false}, 200);
  }

}
