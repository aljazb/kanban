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

  boardPartTable: BoardPartTable[];
  maxDepth: number;
  maxWidth: number;

  constructor() {
    this.boardPartTable = [];
    this.maxDepth = 0;
    this.maxWidth = 0;
  }

  build(boardParts: BoardPart[]): BoardPartTable[] {
    this.buildBoardPartTable(boardParts);
    this.indexBoardPartTable();

    return this.boardPartTable;
  }

  private indexBoardPartTable(): void {

  }

  private buildBoardPartTable(boardParts: BoardPart[]): void {
    boardParts.forEach(boardPart => {
      let bpt = this.recBuild(boardPart, 0);
      this.boardPartTable.push(bpt);
    });
  }

  private recBuild(boardPart: BoardPart, deep: number): BoardPartTable {
    let bpt = new BoardPartTable(boardPart.name, boardPart.maxWip);

    if(this.maxDepth < deep) {
      this.maxDepth = deep;
    }

    if(boardPart.children) {
      bpt.children = [];
      boardPart.children.forEach(boardPart => {
        let cBpt = this.recBuild(boardPart, deep + 1);
        bpt.children.push(cBpt);
      });
    } else {
      this.maxWidth++;
    }

    return bpt;
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
