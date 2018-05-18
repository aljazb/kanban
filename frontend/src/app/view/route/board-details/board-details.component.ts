import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Board} from '../../../api/models/Board';
import {Card} from '../../../api/models/Card';
import {BoardPart} from '../../../api/models/BoardPart';
import {ApiService} from '../../../api/services/api.service';
import {BoardRepresentation} from './utility/board-representation';
import {LoginService} from '../../../api/services/login.service';
import {UserAccount} from '../../../api/models/UserAccount';
import {CardMove} from '../../../api/models/card-move';
import {CardMoveType} from '../../../api/models/enums/CardMoveType';
import {isNullOrUndefined} from 'util';
import {Membership} from '../../../api/models/Membership';
import {ProjectDeleteConfirmationComponent} from '../../components/forms/project-delete-confirmation/project-delete-confirmation.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ToasterService} from 'angular5-toaster/dist';
import {CardMoveConfirmationComponent} from '../../components/forms/card-move-confirmation/card-move-confirmation.component';
import {CardMoveBackConfirmationComponent} from '../../components/forms/card-move-back-confirmation/card-move-back-confirmation.component';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {cTsToDp} from '../../../utility';
import {CardType} from '../../../api/models/enums/card-type';

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

  constructor(private route: ActivatedRoute,
              private api: ApiService,
              private login: LoginService,
              private modalService: NgbModal,
              private toaster: ToasterService) { }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.onInit();
    this.showDisplayOptions = false;
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
  }

  private buildBoardRepresentation(): BoardRepresentation {
    let bp = new BoardRepresentation();
    bp.init(this.board);
    return bp;
  }

  private moveCard(c: Card, from: BoardPart, to: BoardPart) {
    let cm = new CardMove();
    cm.to = to;
    cm.card = c;
    cm.cardMoveType = CardMoveType.VALID;

    let wipExceeded = this.boardRepresentation.willExceedWip(to, from);
    if (wipExceeded) {
      cm.cardMoveType = CardMoveType.INVALID;

      const modalRef = this.modalService.open(CardMoveConfirmationComponent);

      modalRef.result
        .then(value => this.postCardMove(cm));

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

  moveCardBack(c: Card, from: BoardPart, boardParts: BoardPart[]) {
    const modalRef = this.modalService.open(CardMoveBackConfirmationComponent);
    (<CardMoveBackConfirmationComponent> modalRef.componentInstance).setBoardParts(boardParts);

    modalRef.result
      .then(value => this.moveCard(c, from, value))
      .catch(reason => console.log(reason));
  }

  moveCardLeft(c: Card, from: BoardPart, to: BoardPart) {
    this.moveCard(c, from, to)
  }

  moveCardRight(c: Card, from: BoardPart, to: BoardPart) {
    this.moveCard(c, from, to)
  }

  toggleDisplayOptions() : void {
    this.showDisplayOptions = !this.showDisplayOptions;
  }

  private setCriticalCards(n: number) : void {
    if (!isNullOrUndefined(n) && n >= 0) {
      console.log(n);
    } else {
      // reset
    }
  }

}
