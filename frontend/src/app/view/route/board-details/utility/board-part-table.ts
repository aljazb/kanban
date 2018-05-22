import {BoardPart} from '../../../../api/models/BoardPart';

export class BoardPartTable {
  index: number;
  currentWip: number;
  boardPart: BoardPart;

  parent: BoardPartTable;
  children: BoardPartTable[];

  colSpan: number;
  rowSpan: number;
  collapsedRowSpan: number;

  getRowSpan(isCollapsed: boolean): number {
    if(isCollapsed) {
      return this.collapsedRowSpan;
    } else {
      return this.rowSpan;
    }
  }

  constructor(boardPart: BoardPart) {
    this.boardPart = boardPart;
    this.currentWip = 0;
    this.index = -1;
  }

}
