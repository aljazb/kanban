import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Board} from '../../../api/models/Board';
import {Card} from '../../../api/models/Card';
import {BoardPart} from '../../../api/models/BoardPart';
import {ApiService} from '../../../api/api.service';
import {BoardRepresentation} from '../board-edit/utility/board-representation';

@Component({
  selector: 'app-board-details',
  templateUrl: './board-details.component.html',
  styleUrls: ['./board-details.component.css']
})
export class BoardDetailsComponent implements OnInit {

  id: string;

  board: Board;
  rootBoardParts: BoardPart[] = [];

  cards: Card[];

  boardRepresentation: BoardRepresentation;

  constructor(private route: ActivatedRoute,
              private api: ApiService) { }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.api.board.get(this.id).subscribe(board => this.initBoard(board));
  }

  private initBoard(board: Board): void {
    this.board = board;
    this.board.boardParts.forEach(boardPart => {
      if(boardPart.parent == null) {
        this.rootBoardParts.push(boardPart);
      }
    });

    this.sortBoardParts(this.rootBoardParts);
    this.initBoardRepresentation();
  }

  private initBoardRepresentation(): void {
    this.boardRepresentation = new BoardRepresentation();
    this.boardRepresentation.init(this.rootBoardParts);
  }

  private sortBoardParts(boardParts: BoardPart[]): void {
    boardParts.forEach(boardPart => {
      if(boardPart.children != null){
        this.sortBoardParts(boardPart.children);
      }
    });
    boardParts.sort((a, b) => a.orderIndex - b.orderIndex);
  }

}
