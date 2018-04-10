import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Board} from '../../../api/models/Board';
import {Card} from '../../../api/models/Card';
import {BoardPart} from '../../../api/models/BoardPart';
import {ApiService} from '../../../api/api.service';
import {BoardRepresentation} from './utility/board-representation';

@Component({
  selector: 'app-board-details',
  templateUrl: './board-details.component.html',
  styleUrls: ['./board-details.component.css']
})
export class BoardDetailsComponent implements OnInit {

  id: string;

  board: Board;
  rootBoardParts: BoardPart[];

  cards: Card[];

  boardRepresentation: BoardRepresentation;

  constructor(private route: ActivatedRoute,
              private api: ApiService) { }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.api.board.get(this.id).subscribe(board => this.init(board));
  }

  private init(board: Board): void {
    this.board = board;
    this.rootBoardParts = this.buildRootBoardParts(this.board);
    this.boardRepresentation = this.buildBoardRepresentation(this.rootBoardParts);
  }

  private buildRootBoardParts(board: Board): BoardPart[] {
    let rootBoardParts = [];
    board.boardParts.forEach(boardPart => {
      if(boardPart.parent == null) {
        rootBoardParts.push(boardPart);
      }
    });

    this.sortBoardParts(rootBoardParts);

    return rootBoardParts;
  }

  private sortBoardParts(boardParts: BoardPart[]): void {
    boardParts.forEach(boardPart => {
      if(boardPart.children != null){
        this.sortBoardParts(boardPart.children);
      }
    });
    boardParts.sort((a, b) => a.orderIndex - b.orderIndex);
  }

  private buildBoardRepresentation(rootBoardParts: BoardPart[]): BoardRepresentation {
    let bp = new BoardRepresentation();
    bp.init(rootBoardParts);
    return bp;
  }

}
