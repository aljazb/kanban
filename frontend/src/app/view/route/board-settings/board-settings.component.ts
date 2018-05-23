import {Component, OnInit, ViewChild} from '@angular/core';
import {Board} from '../../../api/models/Board';
import {ToasterService} from 'angular5-toaster/dist';
import {ApiService} from '../../../api/services/api.service';
import {ActivatedRoute, Router} from '@angular/router';
import {BoardPart} from '../../../api/models/BoardPart';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {CardMoveRule} from '../../../api/resource/card-move-rules';
import {Location} from '@angular/common';
import {CardMoveRulesPagingComponent} from '../../components/paging/card-move-rules-paging/card-move-rules-paging.component';

@Component({
  selector: 'app-board-settings',
  templateUrl: './board-settings.component.html',
  styleUrls: ['./board-settings.component.css']
})
export class BoardSettingsComponent implements OnInit {

  @ViewChild(CardMoveRulesPagingComponent)
  cardMoveRulePagingComponent: CardMoveRulesPagingComponent;


  id: string;
  board: Board;

  leaves: BoardPart[];

  cardMoveRulesEditing: CardMoveRule[];

  formBoard: FormGroup;
  fcRemainingDays: FormControl;
  fcShowWorkload: FormControl;
  fcShowCardType: FormControl;
  fcShowDueDate: FormControl;


  formRule: FormGroup;
  fcBoardPartFrom: FormControl;
  fcBoardPartTo: FormControl;

  fcRoleDeveloper: FormControl;
  fcRoleKanbanMaster: FormControl;
  fcRoleProductOwner: FormControl;
  fcCanReject: FormControl;
  fcBidirectionalMovement: FormControl;

  fromSelection: BoardPart[];
  toSelection: BoardPart[];


  constructor(private route: ActivatedRoute,
              private router: Router,
              private location: Location,
              private api: ApiService,
              private toaster: ToasterService) {
    this.initFormControl();
    this.initFormGroup();
  }

  private initFormControl(): void {
    this.fcRemainingDays = new FormControl();
    this.fcShowDueDate = new FormControl();
    this.fcShowCardType = new FormControl();
    this.fcShowWorkload = new FormControl();

    this.fcBoardPartFrom = new FormControl(null, Validators.required);
    this.fcBoardPartFrom.valueChanges.subscribe(selectedBp => {
      if(selectedBp) {
        this.toSelection = this.leaves.filter(bp => bp.id != selectedBp.id);
      } else {
        this.toSelection = this.leaves;
      }
    });
    this.fcBoardPartTo = new FormControl(null, Validators.required);
    this.fcBoardPartTo.valueChanges.subscribe(selectedBp => {
      if(selectedBp) {
        this.fromSelection = this.leaves.filter(bp => bp.id != selectedBp.id);
      } else {
        this.fromSelection = this.leaves;
      }
    });

    this.fcRoleDeveloper = new FormControl(false);
    this.fcRoleKanbanMaster = new FormControl(false);
    this.fcRoleProductOwner = new FormControl(false);
    this.fcCanReject = new FormControl(false);
    this.fcCanReject.valueChanges.subscribe(value => {
      if(value) {
        this.fcBoardPartTo.disable();
        this.fcBidirectionalMovement.disable();
      } else {
        this.fcBoardPartTo.enable();
        this.fcBidirectionalMovement.enable();
      }
    });
    this.fcBidirectionalMovement = new FormControl(true);
  }

  private initFormGroup(): void {
    this.formBoard = new FormGroup({
      remainingDays: this.fcRemainingDays,
      showWorkload: this.fcShowWorkload,
      showCardType: this.fcShowCardType,
      showDueDate: this.fcShowDueDate
    });
    this.formRule = new FormGroup({
      from: this.fcBoardPartFrom,
      to: this.fcBoardPartTo,
      roleDeveloper: this.fcRoleDeveloper,
      roleKanbanMaster: this.fcRoleKanbanMaster,
      roleProductOwner: this.fcRoleProductOwner,
      canReject: this.fcCanReject,
      bidirectionalMovement: this.fcBidirectionalMovement
    })
  }

  back(): void {
    this.location.back();
  }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.api.board.get(this.id).subscribe(board => this.init(board));
  }

  private init(board: Board): void {
    this.board = board;
    this.leaves = Board.getLeafParts(this.board.boardParts);

    if(this.board.cardMoveRules == null){
      this.cardMoveRulesEditing = [];
    } else {
      this.board.cardMoveRules.sort((a, b) => b.createdOn - a.createdOn);
      this.cardMoveRulesEditing = Object.assign([], this.board.cardMoveRules);
    }

    this.fromSelection = this.leaves;
    this.toSelection = this.leaves;

    this.fcRemainingDays.patchValue(this.board.remainingDays);
    this.fcShowCardType.patchValue(this.board.showCardType);
    this.fcShowDueDate.patchValue(this.board.showDueDate);
    this.fcShowWorkload.patchValue(this.board.showWorkload);
  }

  handleOnDelete(cardMoveRule: CardMoveRule): void {
    this.cardMoveRulesEditing = this.cardMoveRulesEditing.filter(value => value.id != cardMoveRule.id);
    this.cardMoveRulePagingComponent.refresh();
  }

  onSubmit() {

    let b = new Board();
    b.id = this.board.id;
    b.remainingDays = this.fcRemainingDays.value;
    b.showWorkload = this.fcShowWorkload.value;
    b.showDueDate = this.fcShowDueDate.value;
    b.showCardType = this.fcShowCardType.value;
    b.cardMoveRules = [];

    this.cardMoveRulesEditing.forEach(value => {
      let cmr = new CardMoveRule();
      cmr.id = value.id;
      cmr.from = new BoardPart();
      cmr.from.id = value.from.id;
      cmr.to = new BoardPart();
      cmr.to.id = value.to.id;
      cmr.roleProductOwnerAllowed = value.roleProductOwnerAllowed;
      cmr.roleKanbanMasterAllowed = value.roleKanbanMasterAllowed;
      cmr.roleDeveloperAllowed = value.roleDeveloperAllowed;
      cmr.canReject = value.canReject;
      cmr.bidirectionalMovement = value.bidirectionalMovement;
      b.cardMoveRules.push(cmr);
    });

    console.log(b);

    this.api.board.patch(b).subscribe(value => {
      this.toaster.pop('success', 'Updated board settings');
      this.router.navigate(['board', this.board.id]);
    }, error2 => {
      this.toaster.pop('error', 'Error updating board settings');
    });
  }

  addRule() {
    if(this.formRule.valid) {

      let r: CardMoveRule = new CardMoveRule();

      r.board = new Board();
      r.board.id = this.board.id;

      r.from = this.fcBoardPartFrom.value;
      r.to = this.fcBoardPartTo.value;

      r.canReject = this.fcCanReject.value;
      if(r.canReject) {
        r.from = r.to;
      } else {
        console.log("WTF");
        r.bidirectionalMovement = this.fcBidirectionalMovement.value;
      }
      console.log(r);

      r.roleDeveloperAllowed = this.fcRoleDeveloper.value;
      r.roleKanbanMasterAllowed = this.fcRoleKanbanMaster.value;
      r.roleProductOwnerAllowed = this.fcRoleProductOwner.value;

      this.cardMoveRulesEditing = [r].concat(this.cardMoveRulesEditing);
      this.cardMoveRulePagingComponent.refresh();
    }
  }

}

