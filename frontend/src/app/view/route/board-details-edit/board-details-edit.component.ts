import {Component, OnInit, ViewChild} from '@angular/core';
import {Board} from '../../../api/models/Board';
import {ApiService} from '../../../api/api.service';
import {ActivatedRoute, Router} from '@angular/router';
import {BoardPart} from '../../../api/models/BoardPart';
import {BoardBaseFormComponent} from '../../components/forms/board-form/board-base-form/board-base-form.component';
import {ToasterService} from 'angular5-toaster/dist';

@Component({
  selector: 'app-board-details-edit',
  templateUrl: './board-details-edit.component.html',
  styleUrls: ['./board-details-edit.component.css']
})
export class BoardDetailsEditComponent implements OnInit {

  @ViewChild(BoardBaseFormComponent)
  boardBaseFormComp;

  id: string;
  board: Board;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private api: ApiService,
              private toaster: ToasterService) { }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.api.board.get(this.id).subscribe(board => this.init(board));
  }

  private init(board: Board): void {
    board.boardParts = board.boardParts.filter(value => value.parent == null); // Set root board parts

    this.sortBoardParts(board.boardParts);
    this.buildCardRefs(board);

    this.board = board;
  }

  private sortBoardParts(boardParts: BoardPart[]): void {
    boardParts.sort((a, b) => a.orderIndex - b.orderIndex);
    boardParts.forEach(boardPart => {
      if(boardPart.children) {
        this.sortBoardParts(boardPart.children);
      }
    });
  }

  private buildCardRefs(board: Board): void {
    let allBoardParts = this.buildAllBoardParts(board.boardParts);

    if(board.projects) {
      board.projects.forEach(project => {
        if(Array.isArray(project.cards)) {
          project.cards.forEach(card => {
            let bp = allBoardParts.get(card.boardPart.id);
            if(!Array.isArray(bp.cards)) bp.cards = [];
            bp.cards.push(card);
          })
        }
      });
    }
  }

  private buildAllBoardParts(boardParts: BoardPart[]): Map<string, BoardPart> {
    let bp = new Map<string, BoardPart>();
    boardParts.forEach(boardPart => {
      if(boardPart.children) {
        let cBp = this.buildAllBoardParts(boardPart.children);
        cBp.forEach(child => bp.set(child.id, child));
      } else {
        bp.set(boardPart.id, boardPart);
      }
    });
    return bp;
  }

  private back(): void {
    this.router.navigate([`/board/${this.id}`])
  }

  update(): void {
    console.log(this.board);
    if(this.boardBaseFormComp.isValid()) {
      this.api.board.put(this.board).subscribe(board => {
        this.toaster.pop('success', "Form updated");
        this.back();
      }, error2 => {
        this.toaster.pop('error', "Error updating form");
        this.back();
      });
    } else {
      this.toaster.pop('error', "Invalid form");
    }
  }

}
