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

  private _boardPartIdToIndexMap: Map<string, BoardPartTable>;

  public boardPartTable: BoardPartTable[][];
  public projectTable: ProjectTable[];

  constructor() {
    this._maxDepth = 0;
    this._maxWidth = 0;
    this._boardPartTable = [];

    this._boardPartIdToIndexMap = new Map<string, BoardPartTable>();
    this.projectTable = [];
  }

  init(board: Board): void {
    this.board = board;
    this.buildRootBoardParts();
    this.initBoardPartTable();
    this.setRowSpanBoardPartTable();
    this.initProjectTable();
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
    if(Array.isArray(this.board.projects)){
      this.board.projects.forEach(project => {
        if(Array.isArray(project.cards)) {
          project.cards.forEach(card => {
            this.add(card, project);
          });
        }
      });
    }
  }

  private initProjectTable() {
    if(Array.isArray(this.board.projects)) {
      this.board.projects.sort((a, b) => a.name.localeCompare(b.name));
      this.board.projects.forEach(project => {
        this.projectTable.push(new ProjectTable(project.id, project.name, this._maxWidth));
      });
    }
  }

  private add(card: Card, project: Project): void {
    let pt = this.projectTable.find(value => value.id == project.id);
    let bpt = this._boardPartIdToIndexMap.get(card.boardPart.id);
    pt.add(card, bpt.index);
    this.incWip(bpt);
  }

  private incWip(boardPartTable: BoardPartTable){
    let bpt = boardPartTable;
    while(bpt != null){
      bpt.currentWip++;
      bpt = bpt.parent;
    }
  }

  private decWip(boardPartTable: BoardPartTable){
    let bpt = boardPartTable;
    while(bpt != null){
      bpt.currentWip--;
      bpt = bpt.parent;
    }
  }

  willExceedWip(boardPart: BoardPart) {
    let bpt = this._boardPartIdToIndexMap.get(boardPart.id);
    while (bpt != null) {
      if (bpt.currentWip + 1 > bpt.boardPart.maxWip) {
        return true
      }
      bpt = bpt.parent;
    }

    return false;
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

    let bpt = new BoardPartTable(boardPart);

    if(this._maxDepth < deep) this._maxDepth = deep;

    if(boardPart.children && boardPart.children.length > 0) {
      bpt.children = [];
      bpt.colspan = 0;
      boardPart.children.forEach(boardPart => {
        let cBpt = this.recInitBoardPartTable(boardPart, deep + 1);
        cBpt.parent = bpt;
        bpt.children.push(cBpt);
        bpt.colspan += cBpt.colspan;
      });
    } else {
      bpt.colspan = 1;
      bpt.index = this._maxWidth;
      this._boardPartIdToIndexMap.set(boardPart.id, bpt);
      this._maxWidth++;
    }

    this.boardPartTable[deep].push(bpt);

    return bpt;
  }


  get maxDepth(): number {
    return this._maxDepth;
  }
}
