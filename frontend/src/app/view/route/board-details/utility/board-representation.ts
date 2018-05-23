import {BoardPart} from '../../../../api/models/BoardPart';
import {BoardPartTable} from './board-part-table';
import {Card} from '../../../../api/models/Card';
import {ProjectTable} from './project-table';
import {Board} from '../../../../api/models/Board';
import {Project} from '../../../../api/models/Project';
import {UserAccount} from '../../../../api/models/UserAccount';
import {CardMoveRule} from '../../../../api/resource/card-move-rules';

export class BoardRepresentation {

  private board: Board;
  private rootBoardParts: BoardPart[];
  private _leafBoardParts: BoardPart[];

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
    this.rootBoardParts = this.board.boardParts;

    Board.sortBoardParts(this.rootBoardParts);
    this._leafBoardParts = Board.getLeafParts(this.rootBoardParts);
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
      let cardMoveRulesMap = new Map<string, CardMoveRule[]>();

      if(this.board.cardMoveRules) {
        this.board.cardMoveRules.forEach(cmr => {

          let array: CardMoveRule[] = cardMoveRulesMap.get(cmr.from.id);
          if(array == null) {
            array = [];
            cardMoveRulesMap.set(cmr.from.id, array);
          }
          array.push(cmr);

          if(cmr.bidirectionalMovement) {
            let array: CardMoveRule[] = cardMoveRulesMap.get(cmr.to.id);
            if(array == null) {
              array = [];
              cardMoveRulesMap.set(cmr.to.id, array);
            }
            array.push(cmr);
          }

        });
      }

      this.board.projects.sort((a, b) => a.name.localeCompare(b.name));
      this.board.projects.forEach(project => {
        this.projectTable.push(new ProjectTable(project, this._maxWidth, this.leafBoardParts, cardMoveRulesMap));
      });
    }
  }

  private add(card: Card, project: Project): void {
    let pt = this.projectTable.find(value => value.project.id == project.id);
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

  willExceedWip(to: BoardPart, from: BoardPart=null) {
    let parentIds: Set<string> = new Set<string>();

    while (from != null) {
      parentIds.add(from.id);
      from = from.parent;
    }

    while (to != null) {
      if (to.maxWip != 0) {
        let currentWip = to.currentWip;
        if(parentIds.has(to.id)) {
          currentWip--;
        }

        if(to.maxWip <= currentWip) {
          return true;
        }
      }
      to = to.parent;
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
      boardPartTable.rowSpan = 1;
      boardPartTable.collapsedRowSpan = maxDeep + this.getProjectLength();
      boardPartTable.children.forEach(bpt => {
        this.recSetRowSpanBoardPartTable(bpt, maxDeep - 1);
      });
    } else {
      boardPartTable.rowSpan = maxDeep;
      boardPartTable.collapsedRowSpan = maxDeep + this.getProjectLength();
    }
  }

  private getProjectLength(): number {
    if(this.board.projects == null) {
      return 0;
    } else{
      return this.board.projects.length;
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
      bpt.colSpan = 0;
      boardPart.children.forEach(boardPart => {
        let cBpt = this.recInitBoardPartTable(boardPart, deep + 1);
        cBpt.parent = bpt;
        bpt.children.push(cBpt);
        bpt.colSpan += cBpt.colSpan;
      });
    } else {
      bpt.colSpan = 1;
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

  get leafBoardParts(): BoardPart[] {
    return this._leafBoardParts;
  }
}
