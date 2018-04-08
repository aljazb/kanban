import {Component, OnInit, ViewChild} from '@angular/core';
import {Board} from '../../../api/models/Board';
import {BoardPart} from '../../../api/models/BoardPart';
import {BoardBaseFormComponent} from '../../components/forms/board-form/board-base-form/board-base-form.component';

@Component({
  selector: 'app-board-edit',
  templateUrl: './board-edit.component.html',
  styleUrls: ['./board-edit.component.css']
})
export class BoardEditComponent implements OnInit {

  @ViewChild(BoardBaseFormComponent)
  boardBaseFormComp;

  board: Board = new Board();

  constructor() {

    let bpc = new BoardPart();
    bpc.name = "Part 1.1";
    bpc.maxWip = 2;
    bpc.orderIndex = 0;

    let bp1 = new BoardPart();
    bp1.name = "Part 1";
    bp1.maxWip = 6;
    bp1.orderIndex = 0;
    bp1.children = [bpc];

    let bpc1 = new BoardPart();
    bpc1.name = "Part 2.1";
    bpc1.maxWip = 2;
    bpc1.orderIndex = 0;

    let bpc2 = new BoardPart();
    bpc2.name = "Part 2.2";
    bpc2.maxWip = 2;
    bpc2.orderIndex = 1;

    let bp2 = new BoardPart();
    bp2.name = "Part 2";
    bp2.maxWip = 4;
    bp2.orderIndex = 0;
    bp2.children = [bpc1, bpc2];

    let b = this.board;

    b.name = "Test";
    b.highestPriority = 1;
    b.startDev = 2;
    b.endDev = 3;
    b.acceptanceTesting = 4;
    b.boardParts = [bp1, bp2];

  }

  ngOnInit() {

  }

}
