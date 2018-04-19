import {Component} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {DevTeam} from '../../../../api/models/DevTeam';
import {Card} from '../../../../api/models/Card';
import {ApiService} from '../../../../api/api.service';
import {AbstractControl, FormControl, FormGroup, ValidatorFn, Validators} from '@angular/forms';
import {cDpToTs, cTsToDp, DTDateFormat} from '../../../../utility';
import {FormImpl} from '../form-impl';
import {Project} from '../../../../api/models/Project';

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

  isFormSubmitted: boolean = false;


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
  }

  initFormGroup(): void {
    this.formCard = new FormGroup({
      name: this.fcName,
      description: this.fcDescription,
      workload: this.fcWorkload
    });
  }

  setProject(project) {
    this.project = project
  }

  onSubmit() {
    this.isFormSubmitted = true;
    this.validateForm(this.formCard);
    if (this.formCard.valid) {

      let c = this.card;

      c.name = this.fcName.value;
      c.description = this.fcDescription.value;
      c.workload = this.fcWorkload.value;
      c.project = this.project


      this.activeModal.close(c);
    }
  }

}

