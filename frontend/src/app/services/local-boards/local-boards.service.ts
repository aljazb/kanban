import { Injectable } from '@angular/core';
import {Board} from '../../api/models/Board';
import {JsogService} from 'jsog-typescript';
import * as UUID from 'uuid/v4';
import {BoardPart} from '../../api/models/BoardPart';

@Injectable()
export class LocalBoardsService {

  private STORAGE_KEY: string = "STORED-BOARDS";
  private JSOG = new JsogService();

  boards: Board[];

  constructor() {
    let content = window.localStorage.getItem(this.STORAGE_KEY);
    if(content == null){
      this.boards = [];
    } else {
      this.boards = <Board[]> this.JSOG.deserialize(JSON.parse(content));
    }
  }

  persist(): void {
    let content = JSON.stringify(this.JSOG.serialize(this.boards));
    window.localStorage.setItem(this.STORAGE_KEY, content);
  }

  boardDeleteRef(board: Board): void {
    let index = this.boards.findIndex(b => b == board);
    this.boardDelete(index);
  }

  boardDelete(index: number): void {
    this.boards.splice(index, 1);
    this.persist();
  }

  newBoard(): Board {
    let b = new Board();

    b.id = UUID();
    b.name = "New Board";

    this.boards.push(b);
    this.persist();

    return b;
  }

  getBoards(): Board[] {
    return this.boards;
  }

  copy(board: Board): Board {
    let copyBoard: Board = this.copyBoard(board);

    this.boards.push(copyBoard);
    this.persist();

    return copyBoard;
  }

  private copyBoard(board: Board): Board {
    let b = new Board();

    b.id = UUID();
    b.name = board.name;
    b.boardParts = this.copyBoardParts(b, board, null, board.boardParts);

    return b;
  }

  private copyBoardParts(newBoard: Board, board: Board, newBp: BoardPart, boardParts: BoardPart[]): BoardPart[] {
    let children: BoardPart[] = [];

    if(boardParts){
      boardParts.forEach(bp => {
        let nBp = new BoardPart();
        nBp.id = UUID();
        nBp.orderIndex = bp.orderIndex;
        nBp.name = bp.name;
        nBp.maxWip = bp.maxWip;
        nBp.board = newBoard;
        nBp.leaf = bp.leaf;
        nBp.parent = newBp;

        nBp.children = this.copyBoardParts(newBoard, board, nBp, bp.children);

        if(board.highestPriority == bp.id) {
          newBoard.highestPriority = nBp.id;
        } else if(board.startDev == bp.id) {
          newBoard.startDev = nBp.id;
        }else if(board.endDev == bp.id) {
          newBoard.endDev = nBp.id;
        } else if(board.acceptanceTesting == bp.id) {
          newBoard.acceptanceTesting = nBp.id;
        }

        children.push(nBp);
      });
    }

    return children;
  }

}
