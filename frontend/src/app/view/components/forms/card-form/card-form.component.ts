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

@Component({
  selector: 'app-card-form',
  templateUrl: './card-form.component.html',
  styleUrls: ['./card-form.component.css']
})
export class CardFormComponent extends FormImpl {

  card = new Card();
  project = new Project();

  formCard: FormGroup;
  fcName: FormControl;
  fcDescription: FormControl;
  fcWorkload: FormControl;
  fcColor: FormControl;

  silverBulletLimitExceeded: boolean = false;

  colorSelection: Color[];

  isFormSubmitted: boolean = false;
  leafBoardParts: BoardPart[];

  isSilverbullet: boolean = false;
  boardPartId: string;

  constructor(public activeModal: NgbActiveModal,
              private apiService: ApiService) {
    super();
    this.initFormControls();
    this.initFormGroup();
    this.colorSelection = COLOR_PALETTE;
  }

  initFormControls(): void {
    this.fcName = new FormControl('', Validators.required);
    this.fcDescription = new FormControl('', Validators.required);
    this.fcWorkload = new FormControl('', Validators.pattern('([0-9]+)'));
    this.fcColor = new FormControl(null, Validators.required);
  }

  initFormGroup(): void {
    this.formCard = new FormGroup({
      name: this.fcName,
      description: this.fcDescription,
      workload: this.fcWorkload,
      color: this.fcColor
    });
  }

  setInitialCard(card: Card) {
    this.card = card;
    this.fcName.setValue(card.name);
    this.fcDescription.setValue(card.description);
    this.fcWorkload.setValue(card.workload);
    this.fcColor.setValue(card.color);
    this.isSilverbullet = card.silverBullet;
    this.boardPartId = card.boardPart.id;
    this.project = card.project;

    if (this.isSilverbullet) {
      this.colorSelection = [Color.SILVER];
      this.fcColor.disable();
    } else {
      this.fcColor.patchValue(card.color);
    }
  }

  setProject(project) {
    this.project = project;
    this.apiService.board.get(this.project.board.id).subscribe(value => {
      this.leafBoardParts = Board.getLeafParts(value.boardParts);
      if (this.isSilverbullet) {
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
    this.isSilverbullet = true;
  }

  onSubmit() {
    this.isFormSubmitted = true;
    this.validateForm(this.formCard);
    if (this.formCard.valid) {
      let c = this.card;

      c.name = this.fcName.value;
      c.description = this.fcDescription.value;
      c.workload = this.fcWorkload.value;
      c.project = new Project();
      c.project.id = this.project.id;
      c.boardPart = new BoardPart();
      c.boardPart.id = this.boardPartId;
      c.color = this.fcColor.value;
      c.silverBullet = this.isSilverbullet;

      this.activeModal.close(c);
    }
  }

}

