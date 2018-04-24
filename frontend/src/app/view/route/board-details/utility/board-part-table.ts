import {BoardPart} from '../../../../api/models/BoardPart';

export class BoardPartTable {
  index: number;
  currentWip: number;
  boardPart: BoardPart;

  parent: BoardPartTable;
  children: BoardPartTable[];

  colspan: number;
  rowspan: number;

  constructor(boardPart: BoardPart) {
    this.boardPart = boardPart;
    this.currentWip = 0;
    this.index = -1;
  }

}
