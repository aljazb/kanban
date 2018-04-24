import {Component} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {DevTeam} from '../../../../api/models/DevTeam';
import {Project} from '../../../../api/models/Project';
import {ApiService} from '../../../../api/services/api.service';
import {AbstractControl, FormControl, FormGroup, ValidatorFn, Validators} from '@angular/forms';
import {cDpToTs, cTsToDp, DTDateFormat} from '../../../../utility';
import {FormImpl} from '../form-impl';
import {after} from 'selenium-webdriver/testing';

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
  fcCode: FormControl;
  fcProductBuyer: FormControl;
  fcStartDate: FormControl;
  fcEndDate: FormControl;
  fcDevTeam: FormControl;

  isFormSubmitted: boolean = false;

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
    this.fcCode = new FormControl('', Validators.required);
    this.fcProductBuyer = new FormControl('', Validators.required);
    this.fcStartDate = new FormControl(start, [Validators.required, this.isDateBeforeNow(start)]);
    this.fcStartDate.valueChanges.subscribe(value => this.fcEndDate.patchValue(this.fcEndDate.value));
    this.fcEndDate = new FormControl(end, [Validators.required, this.isDateAfterSource(start, this.fcStartDate)]);
    this.fcDevTeam = new FormControl(null, Validators.required);
  }

  initFormGroup(): void {
    this.formProject = new FormGroup({
      name: this.fcName,
      code: this.fcCode,
      productBuyer: this.fcProductBuyer,
      startDate: this.fcStartDate,
      endDate: this.fcEndDate,
      devTeam: this.fcDevTeam
    });
  }

  setInitialProject(project: Project) {
    this.project = project;
    this.fcName.setValue(project.name);
    this.fcCode.setValue(project.code);
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
      p.code = this.fcCode.value;
      p.productBuyer = this.fcProductBuyer.value;
      p.startDate = cDpToTs(this.fcStartDate.value);
      p.endDate = cDpToTs(this.fcEndDate.value);
      p.devTeam = this.devTeams.filter(e => e.id === this.fcDevTeam.value)[0];

      this.activeModal.close(p);
    }
  }

  isDateBeforeNow(nowDate: DTDateFormat): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      let startDate: DTDateFormat = control.value;
      if(!this.sameOrAfter(nowDate, startDate)) {
        return {'startDateAfterNowDate': {value: control.value}};
      }
      return null;
    };
  }

  isDateAfterSource(nowDate: DTDateFormat, source: FormControl): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      let startDate: DTDateFormat = source.value;
      let endDate: DTDateFormat = control.value;

      if(!this.after(endDate, nowDate)) {
        return {'endDateBeforeNowDate': {value: control.value}};
      }
      if(!this.after(endDate, startDate)) {
        return {'endDateBeforeStartDate': {value: control.value}};
      }
      return null;
    };
  }

  private sameOrAfter(date1: DTDateFormat, date2: DTDateFormat): boolean {
    return date1.year >= date2.year && date1.month >= date2.month && date1.day >= date2.day;
  }

  private after(date1: DTDateFormat, date2: DTDateFormat): boolean {
    return date1.year >= date2.year && date1.month >= date2.month && date1.day > date2.day;
  }

}

