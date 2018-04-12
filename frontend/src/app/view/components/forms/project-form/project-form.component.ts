import {Component} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {DevTeam} from '../../../../api/models/DevTeam';
import {Project} from '../../../../api/models/Project';
import {ApiService} from '../../../../api/api.service';
import {AbstractControl, FormControl, FormGroup, ValidatorFn, Validators} from '@angular/forms';
import {cDpToTs, cTsToDp, DTDateFormat} from '../../../../utility';
import {FormImpl} from '../form-impl';

@Component({
  selector: 'app-project-form',
  templateUrl: './project-form.component.html',
  styleUrls: ['./project-form.component.css']
})
export class ProjectFormComponent extends FormImpl {

  devTeams: DevTeam[];
  project: Project = new Project();

  formProject: FormGroup;
  fcName: FormControl;
  fcProductBuyer: FormControl;
  fcStartDate: FormControl;
  fcEndDate: FormControl;
  fcDevTeam: FormControl;

  isFormSubmitted: boolean = false;
  invalidStartDate: boolean = false;
  invalidEndDate: boolean = false;


  constructor(public activeModal: NgbActiveModal,
              private apiService: ApiService) {
    super();
    this.loadDevTeams();
    this.initFormControls();
    this.initFormGroup();
  }

  initFormControls(): void {
    let start = cTsToDp(Date.now());

    let d = new Date();
    d.setDate(d.getDate() + 1);
    let end = cTsToDp(d.getTime());

    this.fcName = new FormControl('', Validators.required);
    this.fcProductBuyer = new FormControl('', Validators.required);
    this.fcStartDate = new FormControl(start, [Validators.required, this.dateAfterNow(start)]);
    this.fcStartDate.valueChanges.subscribe(value => this.fcEndDate.patchValue(this.fcEndDate.value));
    this.fcEndDate = new FormControl(end, [Validators.required, this.dateAfterValidator(this.fcStartDate)]);
    this.fcDevTeam = new FormControl(null, Validators.required);
  }

  initFormGroup(): void {
    this.formProject = new FormGroup({
      name: this.fcName,
      productBuyer: this.fcProductBuyer,
      startDate: this.fcStartDate,
      endDate: this.fcEndDate,
      devTeam: this.fcDevTeam
    });
  }

  setInitialProject(project: Project) {
    this.project = project;
    this.fcName.setValue(project.name);
    this.fcProductBuyer.setValue(project.productBuyer);
    this.fcStartDate.setValue(cTsToDp(project.startDate));
    this.fcEndDate.setValue(cTsToDp(project.endDate));

    if (project.devTeam)
      this.fcDevTeam.setValue(project.devTeam.id);
  }

  setStartDateState(isDisabled) {
    if(isDisabled){
      this.fcStartDate.disable();
    } else {
      this.fcStartDate.enable();
    }
  }

  loadDevTeams(): void {
    this.apiService.devTeam.getList()
      .subscribe(devTeams => this.devTeams = devTeams.items);
  }

  onSubmit() {
    this.isFormSubmitted = true;
    this.validateForm(this.formProject);
    if (this.formProject.valid) {

      let p = this.project;

      p.name = this.fcName.value;
      p.productBuyer = this.fcProductBuyer.value;
      p.startDate = cDpToTs(this.fcStartDate.value);
      p.endDate = cDpToTs(this.fcEndDate.value);
      p.devTeam = this.devTeams.filter(e => e.id === this.fcDevTeam.value)[0];

      this.activeModal.close(p);
    }
  }

  dateAfterNow(nowDate: DTDateFormat): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      let valid = true;

      let startDate: DTDateFormat = control.value;

      if (nowDate.year > startDate.year) {
        valid = false;
      } else if (nowDate.month > startDate.month) {
        valid = false;
      } else if (nowDate.day > startDate.day) {
        valid = false;
      }

      this.invalidStartDate = !valid;

      return valid ? null : {'invalidDateAfterNow': {value: control.value}};
    };
  }

  dateAfterValidator(source: FormControl): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      let valid = true;

      let startDate: DTDateFormat = source.value;
      let endDate: DTDateFormat = control.value;

      if (startDate.year > endDate.year) {
        valid = false;
      } else if (startDate.month > endDate.month) {
        valid = false;
      } else if (startDate.day >= endDate.day) {
        valid = false;
      }

      this.invalidEndDate = !valid && this.fcEndDate.touched;

      return valid ? null : {'invalidDateAfterStartDate': {value: control.value}};
    };
  }

}

