import {Component, OnInit, ViewChild} from '@angular/core';
import {Board} from '../../../api/models/Board';
import {ToasterService} from 'angular5-toaster/dist';
import {ApiService} from '../../../api/services/api.service';
import {ActivatedRoute, Router} from '@angular/router';
import {BoardPart} from '../../../api/models/BoardPart';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {CardMoveRule} from '../../../api/resource/card-move-rules';
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


  formRule: FormGroup;
  fcBoardPartFrom: FormControl;
  fcBoardPartTo: FormControl;

  fcRoleDeveloper: FormControl;
  fcRoleKanbanMaster: FormControl;
  fcRoleProductOwner: FormControl;
  fcCanReject: FormControl;

  fromSelection: BoardPart[];
  toSelection: BoardPart[];


  constructor(private route: ActivatedRoute,
              private router: Router,
              private api: ApiService,
              private toaster: ToasterService) {
    this.initFormControl();
    this.initFormGroup();
  }

  private initFormControl(): void {
    this.fcRemainingDays = new FormControl();

    this.fcBoardPartFrom = new FormControl(null, Validators.required);
    this.fcBoardPartFrom.valueChanges.subscribe(selectedBp => {
      this.toSelection = this.leaves.filter(bp => bp.id != selectedBp.id);
    });
    this.fcBoardPartTo = new FormControl(null, Validators.required);
    this.fcBoardPartTo.valueChanges.subscribe(selectedBp => {
      this.fromSelection = this.leaves.filter(bp => bp.id != selectedBp.id);
    });

    this.fcRoleDeveloper = new FormControl(false);
    this.fcRoleKanbanMaster = new FormControl(false);
    this.fcRoleProductOwner = new FormControl(false);
    this.fcCanReject = new FormControl(false);
    this.fcCanReject.valueChanges.subscribe(value => {
      if(value) {
        this.fcBoardPartTo.disable();
      } else {
        this.fcBoardPartTo.enable();
      }
    });
  }

  private initFormGroup(): void {
    this.formBoard = new FormGroup({
      remainingDays: this.fcRemainingDays
    });
    this.formRule = new FormGroup({
      from: this.fcBoardPartFrom,
      to: this.fcBoardPartTo,
      roleDeveloper: this.fcRoleDeveloper,
      roleKanbanMaster: this.fcRoleKanbanMaster,
      roleProductOwner: this.fcRoleProductOwner,
      canReject: this.fcCanReject
    })
  }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.api.board.get(this.id).subscribe(board => this.init(board));
  }

  private init(board: Board): void {
    this.board = board;
    this.leaves = Board.getLeafParts(this.board.boardParts);

    this.board.cardMoveRules.sort((a, b) => b.createdOn - a.createdOn);
    this.cardMoveRulesEditing = Object.assign([], this.board.cardMoveRules);

    this.fromSelection = this.leaves;
    this.toSelection = this.leaves;
    this.fcRemainingDays.patchValue(this.board.remainingDays);
  }

  handleOnDelete(cardMoveRule: CardMoveRule): void {
    this.cardMoveRulesEditing = this.cardMoveRulesEditing.filter(value => value.id != cardMoveRule.id);
    this.cardMoveRulePagingComponent.refresh();
  }

  onSubmit() {

    let b = new Board();
    b.id = this.board.id;
    b.remainingDays = this.fcRemainingDays.value;
    b.cardMoveRules = [];

    this.cardMoveRulesEditing.forEach(value => {
      let cmr = new CardMoveRule();
      cmr.id = value.id;
      cmr.from = value.from;
      cmr.to = value.to;
      cmr.roleProductOwnerAllowed = value.roleProductOwnerAllowed;
      cmr.roleKanbanMasterAllowed = value.roleKanbanMasterAllowed;
      cmr.roleDeveloperAllowed = value.roleDeveloperAllowed;
      cmr.canReject = value.canReject;
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
      }

      r.roleDeveloperAllowed = this.fcRoleDeveloper.value;
      r.roleKanbanMasterAllowed = this.fcRoleKanbanMaster.value;
      r.roleProductOwnerAllowed = this.fcRoleProductOwner.value;


      console.log(r);

      this.cardMoveRulesEditing = [r].concat(this.cardMoveRulesEditing);
      this.cardMoveRulePagingComponent.refresh();
    }
  }

}

