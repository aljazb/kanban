import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Board} from '../../../api/models/Board';
import {Card} from '../../../api/models/Card';
import {BoardPart} from '../../../api/models/BoardPart';
import {ApiService} from '../../../api/services/api.service';
import {BoardRepresentation} from './utility/board-representation';
import {LoginService} from '../../../api/services/login.service';
import {CardMove} from '../../../api/models/card-move';
import {CardMoveType} from '../../../api/models/enums/CardMoveType';
import {isNullOrUndefined} from 'util';
import {Membership} from '../../../api/models/Membership';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ToasterService} from 'angular5-toaster/dist';
import {CardMoveConfirmationComponent} from '../../components/forms/card-move-confirmation/card-move-confirmation.component';
import {CardMoveBackConfirmationComponent} from '../../components/forms/card-move-back-confirmation/card-move-back-confirmation.component';
import {FormControl, FormGroup } from '@angular/forms';
import {cTsToDp} from '../../../utility';

@Component({
  selector: 'app-board-details',
  templateUrl: './board-details.component.html',
  styleUrls: ['./board-details.component.css']
})
export class BoardDetailsComponent implements OnInit {

  id: string;
  board: Board;
  boardRepresentation: BoardRepresentation;

  isOwner: boolean;
  isAuthUserKanbanMaster: boolean;

  showDisplayOptions: boolean;

  formDisplayOptions: FormGroup;
  fcCriticalDays: FormControl;
  criticalCardIds: Set<string>;
  criticalAnimation;

  constructor(private route: ActivatedRoute,
              private api: ApiService,
              private login: LoginService,
              private modalService: NgbModal,
              private toaster: ToasterService) { }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.onInit();
    this.showDisplayOptions = false;
    this.criticalCardIds = new Set<string>();
    this.criticalAnimation = null;
    this.initFormControls();
    this.initFormGroup();
  }

  initFormControls(): void {
    this.fcCriticalDays = new FormControl(undefined);
    this.fcCriticalDays.valueChanges.subscribe(value => this.setCriticalCards(value))
  }

  initFormGroup(): void {
    this.formDisplayOptions = new FormGroup({
      criticalDays: this.fcCriticalDays,
    });
  }

  private onInit(): void {
    this.login.getUser().subscribe(user => {
      this.api.board.get(this.id).subscribe(board => {
        this.isOwner = board.owner.id == user.id;
        this.init(board);
      });
    });
  }

  private init(board: Board): void {
    this.board = board;
    this.isAuthUserKanbanMaster = Membership.isKanbanMaster(board.membership);
    this.boardRepresentation = this.buildBoardRepresentation();
    this.fcCriticalDays.patchValue(this.board.remainingDays);
  }

  private buildBoardRepresentation(): BoardRepresentation {
    let bp = new BoardRepresentation();
    bp.init(this.board);
    return bp;
  }

  moveCard(c: Card, from: BoardPart, to: BoardPart, rejected: boolean=false) {
    let cm = new CardMove();
    cm.to = to;
    cm.card = c;
    cm.cardMoveType = CardMoveType.VALID;
    cm.rejected = rejected;

    let wipExceeded = this.boardRepresentation.willExceedWip(to, from);
    if (wipExceeded) {
      cm.cardMoveType = CardMoveType.INVALID;
      const modalRef = this.modalService.open(CardMoveConfirmationComponent);
      modalRef.result
        .then(value => {
          cm.reason = value;
          this.postCardMove(cm)
        }, reason => {});

    } else {
      this.postCardMove(cm);
    }
  }

  private postCardMove(cm: CardMove): void {
    this.api.cardMove.post(cm, false).subscribe(() => {
      this.onInit();
      this.toaster.pop("success", "Moved card");
    }, error2 => {
      this.toaster.pop("error", "Error moving card");
    });
  }

  rejectCard(c: Card, from: BoardPart, boardParts: BoardPart[]) {
    const modalRef = this.modalService.open(CardMoveBackConfirmationComponent);
    (<CardMoveBackConfirmationComponent> modalRef.componentInstance).setBoardParts(boardParts);

    modalRef.result
      .then(value => {
        this.moveCard(c, from, value, true);
      }).catch(reason => console.log(reason));
  }

  toggleDisplayOptions() : void {
    this.showDisplayOptions = !this.showDisplayOptions;
  }

  private setCriticalCards(n: number) : void {
    this.criticalCardIds.clear();
    if (!isNullOrUndefined(n) && n >= 0) {
      let today = cTsToDp(Date.now());
      let dateLimit = new Date(today.year, today.month, today.day + n);
      this.boardRepresentation.projectTable.forEach(pt => {
        pt.cardTables.forEach(ct => {
          ct.cards.forEach(c => {
            if (isNullOrUndefined(c.card.dueDate)) {
              return;
            }

            let cardDateDp = cTsToDp(c.card.dueDate);
            let cardDate = new Date(cardDateDp.year, cardDateDp.month, cardDateDp.day);
            if (cardDate <= dateLimit) {
              this.criticalCardIds.add(c.card.id);
            }
          });
        });
      });
    }
  }

}
