import {Component, OnInit, ViewChild} from '@angular/core';
import {Board} from '../../../api/models/Board';
import {BoardBaseFormComponent} from '../../components/forms/board-form/board-base-form/board-base-form.component';
import {JsogService} from 'jsog-typescript';
import {ApiService} from '../../../api/api.service';

@Component({
  selector: 'app-board-edit',
  templateUrl: './board-edit.component.html',
  styleUrls: ['./board-edit.component.css']
})
export class BoardEditComponent implements OnInit {

  private STORAGE_KEY: string = "STORED-BOARDS";
  private JSOG = new JsogService();

  @ViewChild(BoardBaseFormComponent)
  boardBaseFormComp;

  selectedBoard: Board;
  boards: Board[];

  constructor(private api: ApiService) { }

  ngOnInit() {
    let content = window.localStorage.getItem(this.STORAGE_KEY);
    if(content == null){
      this.boards = [];
    } else {
      this.boards = <Board[]> this.JSOG.deserialize(JSON.parse(content));
    }
  }

  create(): void {
    if(this.boardBaseFormComp.isValid()) {
      this.api.board.post(this.selectedBoard, true).subscribe(board => {
        this.boardDeleteRef(this.selectedBoard);
        this.selectedBoard = null;
      });
    }
  }

  back(): void {
    this.selectedBoard = null;
    this.persist();
  }

  newBoard(): void {
    this.selectedBoard = new Board();
    this.selectedBoard.name = "New Board";
    this.boards.push(this.selectedBoard);
    this.persist();
  }

  selectBoard(board: Board): void {
    this.selectedBoard = board;
  }

  boardDeleteRef(board: Board): void {
    let index = this.boards.findIndex(b => b == board);
    this.boardDelete(index);
  }

  boardDelete(index: number): void {
    this.boards.splice(index, 1);
    this.persist();
  }

  private persist(): void {
    let content = JSON.stringify(this.JSOG.serialize(this.boards));
    window.localStorage.setItem(this.STORAGE_KEY, content);
  }

}
