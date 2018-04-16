import {BoardPart} from '../../../../api/models/BoardPart';
import {BoardPartTable} from './board-part-table';
import {Card} from '../../../../api/models/Card';
import {ProjectTable} from './project-table';
import {Board} from '../../../../api/models/Board';
import {Project} from '../../../../api/models/Project';

export class BoardRepresentation {

  private board: Board;
  private rootBoardParts: BoardPart[];

  private _maxDepth: number;
  private _maxWidth: number;
  private _boardPartTable: BoardPartTable[];

  private _boardPartIdToIndexMap: Map<string, number>;

  public boardPartTable: BoardPartTable[][];
  public projectTable: ProjectTable[];

  constructor() {
    this._maxDepth = 0;
    this._maxWidth = 0;
    this._boardPartTable = [];

    this._boardPartIdToIndexMap = new Map<string, number>();
    this.projectTable = [];
  }

  init(board: Board): void {
    this.board = board;
    this.buildRootBoardParts();
    this.initBoardPartTable();
    this.setRowSpanBoardPartTable();
    this.initCards();
  }

  private buildRootBoardParts(): void {
    this.rootBoardParts = [];
    this.board.boardParts.forEach(boardPart => {
      if(boardPart.parent == null) {
        this.rootBoardParts.push(boardPart);
      }
    });

    this.sortBoardParts(this.rootBoardParts);
  }

  private sortBoardParts(boardParts: BoardPart[]): void {
    boardParts.forEach(boardPart => {
      if(boardPart.children != null){
        this.sortBoardParts(boardPart.children);
      }
    });
    boardParts.sort((a, b) => a.orderIndex - b.orderIndex);
  }

  private initCards(){
    this.board.projects.forEach(project => {
      project.cards.forEach(card => {
        this.add(card, project);
      })
    });
  }

  private add(card: Card, project: Project): void {
    let pt = this.projectTable.find(value => value.id == project.id);
    if(pt == null) {
      pt = new ProjectTable(card.project.id, project.name, this._maxWidth);
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

  private initBoardPartTable(): void {
    this.boardPartTable = [];
    this.rootBoardParts.forEach(boardPart => {
      let bpt = this.recInitBoardPartTable(boardPart, 0);
      this._boardPartTable.push(bpt);
    });
    this._maxDepth += 1;
  }

  private recInitBoardPartTable(boardPart: BoardPart, deep: number): BoardPartTable {
    if(!Array.isArray(this.boardPartTable[deep])) {
      this.boardPartTable[deep] = [];
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

    this.boardPartTable[deep].push(bpt);

    return bpt;
  }
}
