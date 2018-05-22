import {Component} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {Card} from '../../../../api/models/Card';
import {AbstractControl, FormControl, FormGroup, ValidatorFn, Validators} from '@angular/forms';
import {FormImpl} from '../form-impl';
import {Project} from '../../../../api/models/Project';
import {BoardPart} from '../../../../api/models/BoardPart';
import {ApiService} from '../../../../api/services/api.service';
import {Board} from '../../../../api/models/Board';
import {Color} from './utility/color';
import {cDpToTs, cTsToDp, DTDateFormat} from '../../../../utility';
import {UserAccount} from '../../../../api/models/UserAccount';
import {CardType} from '../../../../api/models/enums/card-type';
import {DevTeam} from '../../../../api/models/DevTeam';
import {LoginService} from '../../../../api/services/login.service';
import {Membership} from '../../../../api/models/Membership';
import {isNullOrUndefined} from 'util';

@Component({
  selector: 'app-card-form',
  templateUrl: './card-form.component.html',
  styleUrls: ['./card-form.component.css']
})
export class CardFormComponent extends FormImpl {

  card = new Card();
  project = new Project();

  formCard: FormGroup;
  fcCode: FormControl;
  fcName: FormControl;
  fcCardType: FormControl;
  fcDescription: FormControl;
  fcWorkload: FormControl;
  fcColor: FormControl;
  fcDueDate: FormControl;
  fcAssignedTo: FormControl;
  fcReason: FormControl;

  isFormSubmitted: boolean = false;

  isSilverBullet: boolean = false;
  boardPartId: string;
  boardId: string;

  developerSelection: UserAccount[];
  cardTypeSelection: CardType[];

  constructor(public activeModal: NgbActiveModal,
              private apiService: ApiService,
              private loginService: LoginService) {
    super();
    this.initFormControls();
    this.initFormGroup();

    this.cardTypeSelection = [CardType.MUST_HAVE, CardType.SHOULD_HAVE, CardType.COULD_HAVE, CardType.WONT_HAVE_THIS_TIME];
  }

  initFormControls(): void {
    let start = cTsToDp(Date.now());

    this.fcCode = new FormControl('', Validators.required);
    this.fcName = new FormControl('', Validators.required);
    this.fcCardType = new FormControl(CardType.MUST_HAVE, Validators.required);
    this.fcDescription = new FormControl('', Validators.required);
    this.fcWorkload = new FormControl('');
    this.fcColor = new FormControl('#499dcf', Validators.required);
    this.fcDueDate = new FormControl(undefined, [this.isDateAfterNow(start)]);
    this.fcAssignedTo = new FormControl(null);
    this.fcReason = new FormControl('');
  }

  initFormGroup(): void {
    this.formCard = new FormGroup({
      code: this.fcCode,
      name: this.fcName,
      cardType: this.fcCardType,
      description: this.fcDescription,
      workload: this.fcWorkload,
      color: this.fcColor,
      reason: this.fcReason,
      dueDate: this.fcDueDate,
      assignedTo: this.fcAssignedTo
    });
  }

  setCard(card: Card) {
    this.card = card;
    this.fcCode.setValue(card.code);
    this.fcName.setValue(card.name);
    this.fcCardType.setValue(card.cardType);
    this.fcDescription.setValue(card.description);
    this.fcWorkload.setValue(card.workload);
    this.fcColor.setValue(card.color);
    this.fcAssignedTo.setValue(card.assignedTo == null ? null : card.assignedTo.id);

    this.isSilverBullet = card.silverBullet;
    this.boardPartId = card.boardPart.id;
    this.boardId = this.card.boardPart.board.id;

    this.loadProject(card.project);
  }

  loadProject(project: Project) {
    if(this.project == null || this.project.name == null) {
      this.apiService.project.get(project.id).subscribe(p => {
        this.setProject(p);
      });
    }
  }

  initCardCreation(project: Project) {
    this.setProject(project);

    if(Membership.isKanbanMaster(this.project.membership)) {
      this.fcColor.patchValue(Color.SILVER.hexBackgroundColor);
      this.boardPartId = this.project.highestPriorityId;
      this.isSilverBullet = true;
    } else {
      this.boardPartId = this.project.firstColumnId;
    }
  }

  setProject(project: Project) {
    this.project = project;

    this.apiService.devTeam.get(project.devTeam.id).subscribe(devTeam => {
      this.developerSelection = DevTeam.getDevelopers(devTeam);
    });

    this.setEditingRights();
  }


  setEditingRights() {
    let m = this.project.membership;
    if(Membership.isDeveloper(m)) {
      this.fcName.disable();
      this.fcCode.disable();
      this.fcCardType.disable();
    } else if(Membership.isProductOwner(m)) {
      this.fcAssignedTo.disable();
      this.fcWorkload.disable();
    } else if(Membership.isKanbanMaster(m)) {
      if(this.card.id) {
        this.fcName.disable();
        this.fcCode.disable();
      }
    }
  }

  onSubmit() {
    this.isFormSubmitted = true;
    this.validateForm(this.formCard);
    if (this.formCard.valid) {
      let c = this.card;

      c.code = this.fcCode.value;
      c.name = this.fcName.value;
      c.cardType = this.fcCardType.value;
      c.description = this.fcDescription.value;
      c.workload = this.fcWorkload.value;

      if (!isNullOrUndefined(this.fcDueDate.value)) {
        c.dueDate = cDpToTs(this.fcDueDate.value);
      }

      c.color = this.fcColor.value;
      c.silverBullet = this.isSilverBullet;

      let devId = this.fcAssignedTo.value;
      if(devId) {
        c.assignedTo = this.developerSelection.find(value => value.id == devId);
      } else {
        c.assignedTo = null;
      }

      c.boardPart = new BoardPart();
      c.boardPart.id = this.boardPartId;
      c.boardPart.board = new Board();
      c.boardPart.board.id = this.boardId;

      c.project = new Project();
      c.project.id = this.project.id;

      c.deleteMessage = this.fcReason.value;

      this.activeModal.close(c);
    }
  }

  get isViolatingWip() {
    if (this.isSilverBullet) {
      return this.project.highestPriorityFull;
    } else {
      return this.project.firstColumnFull;
    }
  }

  isDateAfterNow(nowDate: DTDateFormat): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (isNullOrUndefined(control.value)) {
        return null;
      }
      let startDate: DTDateFormat = control.value;
      if(this.sameOrAfter(nowDate, startDate)) {
        return {'beforeNow': {value: control.value}};
      }
      return null;
    };
  }

  private sameOrAfter(date1: DTDateFormat, date2: DTDateFormat): boolean {
    return new Date(date1.year, date1.month, date1.day) >= new Date(date2.year, date2.month, date2.day);
  }
}

