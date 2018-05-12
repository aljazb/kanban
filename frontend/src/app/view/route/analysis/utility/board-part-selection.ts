import {BoardPart} from '../../../../api/models/BoardPart';

export class BoardPartSelection {

  boardPart: BoardPart;
  isActive: boolean = true;


  constructor(boardPart: BoardPart) {
    this.boardPart = boardPart;
  }

  
}
