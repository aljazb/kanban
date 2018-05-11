import {Component} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {Card} from '../../../../api/models/Card';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {FormImpl} from '../form-impl';
import {Project} from '../../../../api/models/Project';
import {BoardPart} from '../../../../api/models/BoardPart';
import {ApiService} from '../../../../api/services/api.service';
import {Board} from '../../../../api/models/Board';
import {Color, COLOR_PALETTE} from './utility/color';
import {cTsToDp} from '../../../../utility';
import {UserAccount} from '../../../../api/models/UserAccount';
import {CardType} from '../../../../api/models/enums/card-type';
import {DevTeam} from '../../../../api/models/DevTeam';

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
  fcAssignedTo: FormControl;

  silverBulletLimitExceeded: boolean = false;

  colorSelection: Color[];

  isFormSubmitted: boolean = false;
  leafBoardParts: BoardPart[];

  isSilverBullet: boolean = false;
  boardPartId: string;

  developerSelection: UserAccount[];
  cardTypeSelection: CardType[];

  constructor(public activeModal: NgbActiveModal,
              private apiService: ApiService) {
    super();
    this.initFormControls();
    this.initFormGroup();
    this.colorSelection = COLOR_PALETTE;

    this.cardTypeSelection = [CardType.MUST_HAVE, CardType.SHOULD_HAVE, CardType.COULD_HAVE];
  }

  initFormControls(): void {
    this.fcCode = new FormControl('', Validators.required);
    this.fcName = new FormControl('', Validators.required);
    this.fcCardType = new FormControl(CardType.MUST_HAVE, Validators.required);
    this.fcDescription = new FormControl('', Validators.required);
    this.fcWorkload = new FormControl('');
    this.fcColor = new FormControl('#499dcf', Validators.required);
    this.fcAssignedTo = new FormControl(null);
  }

  initFormGroup(): void {
    this.formCard = new FormGroup({
      code: this.fcCode,
      name: this.fcName,
      cardType: this.fcCardType,
      description: this.fcDescription,
      workload: this.fcWorkload,
      color: this.fcColor,
      assignedTo: this.fcAssignedTo
    });
  }

  setInitialCard(card: Card) {
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

    this.loadProject(card.project);
    //this.project = card.project;

    if (this.isSilverBullet) {
      this.colorSelection = [Color.SILVER];
      this.fcColor.disable();
    } else {
      this.fcColor.patchValue(card.color);
    }
  }

  loadProject(project: Project) {
    if(this.project == null || this.project.name == null) {
      this.apiService.project.get(project.id).subscribe(value => {
        this.setProject(value);
      });
    }
  }

  setProject(project) {
    this.project = project;

    this.apiService.devTeam.get(project.devTeam.id).subscribe(devTeam => {
      this.developerSelection = DevTeam.getDevelopers(devTeam);
    });

    this.apiService.board.get(this.project.board.id).subscribe(value => {
      this.leafBoardParts = Board.getLeafParts(value.boardParts);
      if (this.isSilverBullet) {
        this.colorSelection = [Color.SILVER];
        this.fcColor.patchValue(Color.SILVER.hexBackgroundColor);
        this.fcColor.disable();
        this.boardPartId = this.leafBoardParts[value.highestPriority].id;
      } else {
        this.boardPartId = this.leafBoardParts[0].id;
      }

    });
  }

  setIsSilverBullet() {
    this.isSilverBullet = true;
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

      c.project = new Project();
      c.project.id = this.project.id;

      this.activeModal.close(c);
    }
  }

}

