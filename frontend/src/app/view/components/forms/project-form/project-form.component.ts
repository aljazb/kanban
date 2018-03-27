import {Component, Input} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {DevTeam} from '../../../../api/models/DevTeam';
import {Project} from '../../../../api/models/Project';
import {ApiService} from '../../../../api/api.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Utility} from '../../../../utility';

@Component({
  selector: 'app-project-form',
  templateUrl: './project-form.component.html',
  styleUrls: ['./project-form.component.css']
})
export class ProjectFormComponent {

  devTeams: DevTeam[];

  formProject: FormGroup;

  fcName: FormControl;
  fcProductBuyer: FormControl;
  fcStartDate: FormControl;
  fcEndDate: FormControl;
  fcDevTeam: FormControl;

  constructor(
    public activeModal: NgbActiveModal,
    private apiService:ApiService)
  {
    this.loadDevTeams();
    this.initFormControls();
    this.initFormGroup();
  }

  initFormControls(): void {
    this.fcName = new FormControl('', Validators.required);
    this.fcProductBuyer = new FormControl('', Validators.required);
    this.fcStartDate = new FormControl(Date.now(), Validators.required);
    this.fcEndDate = new FormControl(Date.now(), Validators.required);
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
    this.fcName.setValue(project.name);
    this.fcProductBuyer.setValue(project.productBuyer);
    this.fcStartDate.setValue(Utility.cTsToDp(project.startDate));
    this.fcEndDate.setValue(Utility.cTsToDp(project.endDate));

    if(project.devTeam)
      this.fcDevTeam.setValue(project.devTeam.id);
  }

  loadDevTeams(): void {
    this.apiService.devTeam.getList()
      .subscribe(devTeams => this.devTeams = devTeams.items);
  }

  onSubmit() {
    if(this.formProject.valid) {

      let p = new Project();
      p.name = this.fcName.value;
      p.productBuyer = this.fcProductBuyer.value;
      p.startDate = Utility.cDpToTs(this.fcStartDate.value);
      p.endDate = Utility.cDpToTs(this.fcEndDate.value);
      p.devTeam = this.devTeams.filter(e => e.id === this.fcDevTeam.value)[0];

      this.activeModal.dismiss(p)
    }
  }

}
