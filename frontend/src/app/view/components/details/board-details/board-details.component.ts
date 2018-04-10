import {Component, Input, OnInit} from '@angular/core';
import {Board} from '../../../../api/models/Board';
import {BoardPart} from '../../../../api/models/BoardPart';
import {Card} from '../../../../api/models/Card';

@Component({
  selector: 'app-board-details',
  templateUrl: './board-details.component.html',
  styleUrls: ['./board-details.component.css']
})
export class BoardDetailsComponent implements OnInit {

  @Input()
  board: Board;

  boardParts: BoardPart[];
  cards: Card[];

  boardPartsCardsMap = {};

  constructor() { }

  ngOnInit() {


  }

}

class BoardBuilder {

  private boardParts: BoardPart[];
  private _boardPartCardMap = {};
  private _boardPartTable: BoardPartTable[];
  maxDepth: number;

  constructor() {
    this._boardPartTable = [];
    this.maxDepth = 0;
  }

  build(boardParts: BoardPart[]): void {
    this.boardParts = boardParts;

    this.buildBoardPartTable();
    this.setRowSpanBoardPartTable();
  }

  private setRowSpanBoardPartTable(): void {
    this.boardPartTable.forEach(boardPart => {

    });
  }

  private buildBoardPartTable(): void {
    this.boardParts.forEach(boardPart => {
      let bpt = this.recBuild(boardPart, 1);
      this._boardPartTable.push(bpt);
    });
  }

  private recBuild(boardPart: BoardPart, deep: number): BoardPartTable {
    let bpt = new BoardPartTable(boardPart.name, boardPart.maxWip);

    if(this.maxDepth < deep) this.maxDepth = deep;

    if(boardPart.children || boardPart.children.length == 0) {
      bpt.children = [];
      boardPart.children.forEach(boardPart => {
        let cBpt = this.recBuild(boardPart, deep + 1);
        bpt.children.push(cBpt);
        bpt.colspan += cBpt.colspan;
      });
    } else {
      bpt.colspan = 1;
      this._boardPartCardMap[boardPart.id] = new CardsTable(boardPart.orderIndex);
    }

    return bpt;
  }

  get boardPartCardMap(): {} {
    return this._boardPartCardMap;
  }

  get boardPartTable(): BoardPartTable[] {
    return this._boardPartTable;
  }

}

class BoardPartTable {
  name: string;
  wip: number;

  children: BoardPartTable[];

  colspan: number;
  rowspan: number;

  constructor(name: string, wip: number) {
    this.name = name;
    this.wip = wip;
  }
}

class CardsTable {
  orderIndex: number;
  cards: Card[];

  constructor(orderIndex: number) {
    this.orderIndex = orderIndex;
    this.cards = [];
  }
}
