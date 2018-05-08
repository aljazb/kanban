import {Component, OnInit, ViewChild} from '@angular/core';
import {Board} from '../../../api/models/Board';
import {ApiService} from '../../../api/services/api.service';
import {ActivatedRoute, Router} from '@angular/router';
import {BoardPart} from '../../../api/models/BoardPart';
import {BoardBaseFormComponent} from '../../components/forms/board-form/board-base-form/board-base-form.component';
import {ToasterService} from 'angular5-toaster/dist';
import {Location} from '@angular/common';
import {LocalBoardsService} from '../../../services/local-boards/local-boards.service';
import {CardMoveConfirmationComponent} from '../../components/forms/card-move-confirmation/card-move-confirmation.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ColumnWipViolationConfirmationComponent} from '../../components/forms/column-wip-violation-confirmation/column-wip-violation-confirmation.component';

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

  initialMaxWips: Map<string, number>;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private api: ApiService,
              private toaster: ToasterService,
              private location: Location,
              private localBoard: LocalBoardsService,
              private modalService: NgbModal) { }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.api.board.get(this.id).subscribe(board => this.init(board));
  }

  private init(board: Board): void {
    board.boardParts = board.boardParts.filter(value => value.parent == null); // Set root board parts

    this.sortBoardParts(board.boardParts);
    this.buildCardRefs(board);

    this.board = board;

    this.initialMaxWips = new Map<string, number>();
    this.board.boardParts.forEach(bp => {
      this.initialMaxWips.set(bp.id, bp.maxWip);
    });
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

  copy(): void {
    let b = this.localBoard.copy(this.board);
    this.toaster.pop("success", "Board was copied");
    this.router.navigate(['/board/edit'], {fragment: b.id})
  }

  back(): void {
    this.location.back();
  }

  private checkCurrentWipExceeded(): Promise<any> {
    let exceedingCols: string[] = [];
    this.board.boardParts.forEach(bp => {
      if (this.initialMaxWips.has(bp.id) && this.initialMaxWips.get(bp.id) != bp.maxWip && bp.currentWip > bp.maxWip) {
        exceedingCols.push(bp.name);
      }
    });

    const modalRef = this.modalService.open(ColumnWipViolationConfirmationComponent);
    (<ColumnWipViolationConfirmationComponent> modalRef.componentInstance).setExceedingColumns(exceedingCols);

    return modalRef.result;
  }

  update(): void {
    if(this.boardBaseFormComp.isValid()) {

      this.checkCurrentWipExceeded().then(() => {
        this.api.board.put(this.board).subscribe(board => {
          this.toaster.pop('success', "Form updated");
          this.back();
        }, error2 => {
          this.toaster.pop('error', "Error updating form");
          this.back();
        });
      });
    } else {
      this.toaster.pop('error', "Invalid form");
    }
  }

}
