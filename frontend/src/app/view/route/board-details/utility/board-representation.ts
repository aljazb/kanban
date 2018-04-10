import {BoardPart} from '../../../../api/models/BoardPart';
import {BoardPartTable} from './board-part-table';
import {Card} from '../../../../api/models/Card';
import {ProjectTable} from './project-table';

export class BoardRepresentation {

  private _maxDepth: number;
  private _maxWidth: number;
  private _boardPartTable: BoardPartTable[];

  private _boardPartIdToIndexMap: Map<string, number>;

  public table: BoardPartTable[][];
  public projectTable: ProjectTable[];

  constructor() {
    this._maxDepth = 0;
    this._maxWidth = 0;
    this._boardPartTable = [];

    this._boardPartIdToIndexMap = new Map<string, number>();
    this.projectTable = [];
  }

  init(boardParts: BoardPart[]): void {
    this.initBoardPartTable(boardParts);
    this.setRowSpanBoardPartTable();
  }

  add(card: Card): void {
    let pt = this.projectTable.find(value => value.id == card.project.id);
    if(pt == null) {
      pt = new ProjectTable(card.project.id, "Test - " + this.projectTable.length);
      this.projectTable.push(pt);
    }

    let index = this._boardPartIdToIndexMap.get(card.boardPart.id);
    pt.add(card, index);
  }

  private setRowSpanBoardPartTable(): void {
    this._boardPartTable.forEach(boardPartTable => {
      this.recSetRowSpanBoardPartTable(boardPartTable, this._maxDepth);
    });
  }

  private recSetRowSpanBoardPartTable(boardPartTable: BoardPartTable, maxDeep: number): void {
    if(boardPartTable.children) {
      boardPartTable.rowspan = 1;
      boardPartTable.children.forEach(bpt => {
        this.recSetRowSpanBoardPartTable(bpt, maxDeep - 1);
      });
    } else {
      boardPartTable.rowspan = maxDeep;
    }
  }

  private initBoardPartTable(boardParts: BoardPart[]): void {
    this.table = [];
    boardParts.forEach(boardPart => {
      let bpt = this.recInitBoardPartTable(boardPart, 0);
      this._boardPartTable.push(bpt);
    });
    this._maxDepth += 1;
  }

  private recInitBoardPartTable(boardPart: BoardPart, deep: number): BoardPartTable {
    if(!Array.isArray(this.table[deep])) {
      this.table[deep] = [];
    }

    let bpt = new BoardPartTable(boardPart.name, boardPart.maxWip);

    if(this._maxDepth < deep) this._maxDepth = deep;

    if(boardPart.children && boardPart.children.length > 0) {
      bpt.children = [];
      bpt.colspan = 0;
      boardPart.children.forEach(boardPart => {
        let cBpt = this.recInitBoardPartTable(boardPart, deep + 1);
        bpt.children.push(cBpt);
        bpt.colspan += cBpt.colspan;
      });
    } else {
      bpt.colspan = 1;
      this._boardPartIdToIndexMap.set(boardPart.id, this._maxWidth);
      this._maxWidth++;
    }

    this.table[deep].push(bpt);

    return bpt;
  }
}
