import {Component, OnDestroy, OnInit} from '@angular/core';
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
import {SubTask} from '../../../api/models/sub-task';
import {CollapsedSetting} from './utility/collapsed-setting';
import {BoardPartTable} from './utility/board-part-table';
import {CardTable} from './utility/card-table';

@Component({
  selector: 'app-board-details',
  templateUrl: './board-details.component.html',
  styleUrls: ['./board-details.component.css']
})
export class BoardDetailsComponent implements OnInit, OnDestroy {

  id: string;
  board: Board;
  boardRepresentation: BoardRepresentation;

  isOwner: boolean;
  isAuthUserKanbanMaster: boolean;
  isAuthUserDeveloper: boolean;

  collapsedSettings: Map<string, boolean>;

  showDisplayOptions: boolean;

  formDisplayOptions: FormGroup;
  fcCriticalDays: FormControl;
  criticalCardIds: Set<string>;

  constructor(private route: ActivatedRoute,
              private api: ApiService,
              private login: LoginService,
              private modalService: NgbModal,
              private toaster: ToasterService)
  {
    this.showDisplayOptions = false;
    this.criticalCardIds = new Set<string>();
    this.collapsedSettings = new Map<string, boolean>();

    this.initFormControls();
    this.initFormGroup();
  }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.initCollapsedSettings();
    this.onInit();
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
    this.isAuthUserDeveloper = Membership.isDeveloper(board.membership);
    this.boardRepresentation = this.buildBoardRepresentation();
    this.boardRepresentation.projectTable.forEach(pt => {
      pt.cardTables.forEach(ct => {
        ct.cards.forEach(c => {
          if (!isNullOrUndefined(c.card.subTasks)) {
            c.card.subTasks = c.card.subTasks.sort((a, b) => a.createdOn - b.createdOn);
          }
        });
      });
    });
    this.fcCriticalDays.patchValue(this.board.remainingDays);
    this.validateCollapseSettings();
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

      modalRef.result.then(value => {
          cm.reason = value;
          this.postCardMove(cm);
        }, reason => { console.log(reason)});

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

  updateCompleted(subTask: SubTask) {
    subTask.completed = !subTask.completed;

    this.api.subTask.put(subTask, true).subscribe(value => {
      console.log(value)
    }, error2 => {
      this.toaster.pop("error", "Error updating subtask");
    });
  }

  subTaskMoveWarning(card: Card) {
    if (card.boardPart.leafNumber + 1 == this.board.acceptanceTesting)
      for (let s of card.subTasks)
        if (!s.completed)
          return true;
    return false;
  }

  private setCriticalCards(n: number) : void {
    this.criticalCardIds.clear();
    if (!isNullOrUndefined(n) && n >= 0) {
      let today = cTsToDp(Date.now());
      let dateLimit = new Date(today.year, today.month, today.day + n);
      let lastLeaf = this.boardRepresentation.leafBoardParts[this.boardRepresentation.leafBoardParts.length - 1];
      this.boardRepresentation.projectTable.forEach(pt => {
        pt.cardTables.forEach(ct => {
          if (ct.currentBoardPart.leafNumber == lastLeaf.leafNumber) {
            return;
          }
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

  initCollapsedSettings(): void {
    let key = this.getCollapsedSettingsKey(this.id);
    let content: CollapsedSetting[] = JSON.parse(window.localStorage.getItem(key));
    if(content) {
      content.forEach(iten => {
        this.collapsedSettings.set(iten.id, iten.value);
      });
    }
  }

  validateCollapseSettings(): void {
    this.board.boardParts.forEach(bp => {
      this.validateCollapseSettingsRec(bp);
    });
  }

  private validateCollapseSettingsRec(boardPart: BoardPart) {
    let isCollapsed = this.getIsCollapsed(boardPart);
    if(!isCollapsed) {
      this.toggleIsCollapsed(boardPart, isCollapsed);
    } else {
      if(BoardPart.hasChildren(boardPart)) {
        boardPart.children.forEach(bp => {
          this.validateCollapseSettingsRec(bp);
        });
      }
    }
  }

  persistCollapsedSettings(): void {
    if(this.collapsedSettings) {
      let key = this.getCollapsedSettingsKey(this.id);
      let content: CollapsedSetting[] = [];

      this.collapsedSettings.forEach((value, key) => {
        content.push(new CollapsedSetting(key, value));
      });

      let json = JSON.stringify(content);

      window.localStorage.setItem(key, json);
    }
  }

  filterCardTable(ct: CardTable[]): CardTable[] {
    if(ct) {
      return ct.filter(value => !this.getIsCollapsed(value.currentBoardPart));
    } else {
      return [];
    }
  }

  filterBoardPartTable(ct: BoardPartTable[]): BoardPartTable[] {
    if(ct) {
      return ct.filter(value => value.boardPart == null || !this.getIsCollapsed(value.boardPart.parent));
    } else {
      return [];
    }
  }

  getIsCollapsed(boardPart: BoardPart): boolean {
    if(boardPart != null) {
      let isCollapsed = this.collapsedSettings.get(boardPart.id);
      if(isCollapsed != null) {
        return isCollapsed;
      }
    }
    return false;
  }

  toggleIsCollapsed(boardPart: BoardPart, collapsed=null): void {
    let isCollapsed = !this.getIsCollapsed(boardPart);
    if(collapsed != null) {
      isCollapsed = collapsed;
    }
    this.collapsedSettings.set(boardPart.id, isCollapsed);

    if(BoardPart.hasChildren(boardPart)) {
      boardPart.children.forEach(bp => {
        this.toggleIsCollapsed(bp, isCollapsed);
      });
    }
  }

  private getCollapsedSettingsKey(id: string) {
    return "CS_" + id;
  }

  ngOnDestroy(): void {
    this.persistCollapsedSettings();
  }

}
