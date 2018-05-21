import {Component} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {Card} from '../../../../api/models/Card';
import {AbstractControl, FormControl, FormGroup, ValidatorFn, Validators} from '@angular/forms';
import {FormImpl} from '../form-impl';
import {Project} from '../../../../api/models/Project';
import {BoardPart} from '../../../../api/models/BoardPart';
import {ApiService} from '../../../../api/services/api.service';
import {Board} from '../../../../api/models/Board';
import {cDpToTs, cTsToDp, DTDateFormat} from '../../../../utility';
import {UserAccount} from '../../../../api/models/UserAccount';
import {CardType} from '../../../../api/models/enums/card-type';
import {DevTeam} from '../../../../api/models/DevTeam';
import {LoginService} from '../../../../api/services/login.service';
import {Membership} from '../../../../api/models/Membership';
import {isNullOrUndefined} from 'util';
import {SubTask} from '../../../../api/models/sub-task';

@Component({
  selector: 'app-subtask-form',
  templateUrl: './subtask-form.component.html',
  styleUrls: ['./subtask-form.component.css']
})
export class SubtaskFormComponent extends FormImpl {

  project = new Project();
  card = new Card();
  subtask = new SubTask();

  formSubtask: FormGroup;
  fcName: FormControl;
  fcDescription: FormControl;
  fcWorkload: FormControl;
  fcAssignedTo: FormControl;
  fcCompleted: FormControl;

  isFormSubmitted: boolean = false;

  developerSelection: UserAccount[];

  constructor(public activeModal: NgbActiveModal,
              private apiService: ApiService,
              private loginService: LoginService) {
    super();
    this.initFormControls();
    this.initFormGroup();
  }

  initFormControls(): void {
    let start = cTsToDp(Date.now());

    this.fcName = new FormControl('', Validators.required);
    this.fcDescription = new FormControl('', Validators.required);
    this.fcWorkload = new FormControl('');
    this.fcAssignedTo = new FormControl(null);
    this.fcCompleted = new FormControl(false);
  }

  initFormGroup(): void {
    this.formSubtask = new FormGroup({
      name: this.fcName,
      description: this.fcDescription,
      workload: this.fcWorkload,
      assignedTo: this.fcAssignedTo,
      completed: this.fcCompleted
    });
  }

  setSubtask(subtask: SubTask) {
    this.subtask = subtask;
    this.fcName.setValue(subtask.name);
    this.fcDescription.setValue(subtask.description);
    this.fcWorkload.setValue(subtask.workingHours);
    this.fcAssignedTo.setValue(subtask.assignedTo == null ? null : subtask.assignedTo.id);
    this.fcCompleted.setValue(subtask.completed);

    this.loadCard(subtask.card);
  }

  loadProject(project: Project) {
    if(this.project == null || this.project.name == null) {
      this.apiService.project.get(project.id).subscribe(p => {
        this.setProject(p);
      });
    }
  }

  loadCard(card: Card) {
    if(this.card == null || this.card.name == null) {
      this.apiService.card.get(card.id).subscribe(c => {
        this.setCard(c);
      });
    }
  }

  setCard(card: Card) {
    this.card = card;
  }

  initSubtaskCreation(project: Project, card: Card) {
    this.setProject(project);
    this.setCard(card);
  }

  setProject(project: Project) {
    this.project = project;

    this.apiService.devTeam.get(project.devTeam.id).subscribe(devTeam => {
      this.developerSelection = DevTeam.getDevelopers(devTeam);
    });
  }

  onSubmit() {
    this.isFormSubmitted = true;
    this.validateForm(this.formSubtask);
    if (this.formSubtask.valid) {
      let s = this.subtask;

      s.name = this.fcName.value;
      s.description = this.fcDescription.value;
      s.workingHours = this.fcWorkload.value;
      s.completed = this.fcCompleted.value;

      let devId = this.fcAssignedTo.value;
      if(devId) {
        s.assignedTo = this.developerSelection.find(value => value.id == devId);
      } else {
        s.assignedTo = null;
      }

      s.card = new Card()
      s.card.id = this.card.id;

      this.activeModal.close(s);
    }
  }
}

