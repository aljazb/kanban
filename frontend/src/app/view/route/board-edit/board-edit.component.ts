import {Component, OnInit, ViewChild} from '@angular/core';
import {Board} from '../../../api/models/Board';
import {BoardBaseFormComponent} from '../../components/forms/board-form/board-base-form/board-base-form.component';
import {JsogService} from 'jsog-typescript';

@Component({
  selector: 'app-board-edit',
  templateUrl: './board-edit.component.html',
  styleUrls: ['./board-edit.component.css']
})
export class BoardEditComponent implements OnInit {

  private STORAGE_KEY: string = "STORED-BOARDS";
  private jsog = new JsogService();

  @ViewChild(BoardBaseFormComponent)
  boardBaseFormComp;

  board: Board;
  boards: Board[];

  constructor() { }

  ngOnInit() {
    let content = window.localStorage.getItem(this.STORAGE_KEY);
    if(content == null){
      this.boards = [];
    } else {
      this.boards = <Board[]> this.jsog.deserialize(JSON.parse(content));
    }
  }

  create(): void {
    console.log(this.board);
    if(this.boardBaseFormComp.isValid()) {

    }
  }

  back(): void {
    this.board = null;
    this.persist();
  }

  newBoard(): void {
    this.board = new Board();
    this.board.name = "New Board";
    this.boards.push(this.board);
  }

  goToBoardDetails(board: Board): void {
    this.board = board;
  }

  boardDelete(index: number): void {
    this.boards.splice(index, 1);
  }

  private persist(): void {
    let content = JSON.stringify(this.jsog.serialize(this.boards));
    window.localStorage.setItem(this.STORAGE_KEY, content);
  }

}
