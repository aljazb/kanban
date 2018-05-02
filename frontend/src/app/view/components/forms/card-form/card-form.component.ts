import {Component} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {Card} from '../../../../api/models/Card';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {FormImpl} from '../form-impl';
import {Project} from '../../../../api/models/Project';
import {BoardPart} from '../../../../api/models/BoardPart';
import {ApiService} from '../../../../api/services/api.service';
import {Board} from '../../../../api/models/Board';

@Component({
  selector: 'app-card-form',
  templateUrl: './card-form.component.html',
  styleUrls: ['./card-form.component.css']
})
export class CardFormComponent extends FormImpl {

  card = new Card();
  project = new Project();

  colors = ['#F1DEDE', '#D496A7', '#5D576B', '#6CD4FF', '#FE938C'];

  formCard: FormGroup;
  fcName: FormControl;
  fcDescription: FormControl;
  fcWorkload: FormControl;
  fcColor: FormControl;

  isFormSubmitted: boolean = false;
  leafBoardParts: BoardPart[];

  isSilverbullet: boolean = false;
  boardPartId: string;

  constructor(public activeModal: NgbActiveModal,
              private apiService: ApiService) {
    super();
    this.initFormControls();
    this.initFormGroup();
  }

  initFormControls(): void {
    this.fcName = new FormControl('', Validators.required);
    this.fcDescription = new FormControl('', Validators.required);
    this.fcWorkload = new FormControl('');
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

  setProject(project) {
    this.project = project;
    this.apiService.board.get(this.project.board.id).subscribe(value => {
      this.leafBoardParts = Board.getLeafParts(value.boardParts);
      Board.sortBoardParts(this.leafBoardParts);
      if (this.isSilverbullet) {
        this.boardPartId = this.leafBoardParts[value.highestPriority].id;
      } else {
        this.boardPartId = this.leafBoardParts[0].id;
      }
      // this.fcBoardPart.patchValue(this.leafBoardParts[0].id);
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

      this.activeModal.close(c);
    }
  }

}

