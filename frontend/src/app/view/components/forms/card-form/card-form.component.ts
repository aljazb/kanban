import {Component} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {Card} from '../../../../api/models/Card';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {FormImpl} from '../form-impl';
import {Project} from '../../../../api/models/Project';
import {BoardPart} from '../../../../api/models/BoardPart';
import {ApiService} from '../../../../api/services/api.service';

@Component({
  selector: 'app-card-form',
  templateUrl: './card-form.component.html',
  styleUrls: ['./card-form.component.css']
})
export class CardFormComponent extends FormImpl {

  card = new Card();
  project = new Project()

  formCard: FormGroup;
  fcName: FormControl;
  fcDescription: FormControl;
  fcWorkload: FormControl;
  fcBoardPart: FormControl;

  isFormSubmitted: boolean = false;
  leafBoardParts: BoardPart[];

  constructor(public activeModal: NgbActiveModal,
              private apiService: ApiService) {
    super();
    this.initFormControls();
    this.initFormGroup();
  }

  initFormControls(): void {
    this.fcName = new FormControl('', Validators.required);
    this.fcDescription = new FormControl('', Validators.required);
    this.fcWorkload = new FormControl('', Validators.required);
    this.fcBoardPart = new FormControl(null);
    this.fcBoardPart.valueChanges.subscribe(id => this.selectBoardPart(id));
  }

  initFormGroup(): void {
    this.formCard = new FormGroup({
      name: this.fcName,
      description: this.fcDescription,
      workload: this.fcWorkload,
      boardPart: this.fcBoardPart
    });
  }

  selectBoardPart(id: string): BoardPart {
    return this.leafBoardParts.find(value => value.id == id);
  }

  setProject(project) {
    this.project = project;
    this.apiService.board.get(this.project.board.id).subscribe(value => {
      this.leafBoardParts = this.getLeafParts(value.boardParts);
      this.fcBoardPart.patchValue(this.leafBoardParts[0].id);
    });
  }

  private getLeafParts(boardParts: BoardPart[]): BoardPart[] {
    let array = [];

    boardParts.forEach(boardPart => {
      if (boardPart.leaf) {
        array.push(boardPart);
      } else {
        let cArray = this.getLeafParts(boardPart.children);
        array = array.concat(cArray);
      }
    });
    return array;
  }

  onSubmit() {
    this.isFormSubmitted = true;
    this.validateForm(this.formCard);
    if (this.formCard.valid) {

      let c = this.card;

      c.name = this.fcName.value;
      c.description = this.fcDescription.value;
      c.workload = this.fcWorkload.value;
      c.project = this.project;


      this.activeModal.close(c);
    }
  }

}

