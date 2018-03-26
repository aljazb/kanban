import {Component, Input} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {DevTeam} from '../../../../api/models/DevTeam';
import {Project} from '../../../../api/models/Project';
import {ApiService} from '../../../../api/Api';

@Component({
  selector: 'app-project-creation-form',
  templateUrl: './project-edit-form.component.html',
  styleUrls: ['./project-edit-form.component.css']
})
export class ProjectEditFormComponent {

  devTeams: DevTeam[];
  project: Project;
  startDate = {};
  endDate = {};
  emptyFields = false;
  earlyDate = false;
  dateSwitch = false;

  constructor(
    public activeModal: NgbActiveModal,
    private apiService:ApiService)
  {
    this.loadContent()
  }

  loadContent(): void {
    this.getDevTeams();
  }

  getDevTeams(): void {
    this.apiService.devTeam.getList()
      .subscribe(devTeams => this.devTeams = devTeams.items);
  }

  editProject() {
    this.emptyFields = this.earlyDate = this.dateSwitch = false;
    if (!this.project.startDate || !this.project.endDate || !this.project.name || !this.project.productBuyer) {
      this.emptyFields = true;
    } else {
      this.project.startDate = this.toDateFormat(this.project.startDate);
      this.project.endDate = this.toDateFormat(this.project.endDate);
      if (this.project.startDate.setHours(0,0,0,0) < (new Date()).setHours(0,0,0,0)) {
        this.earlyDate = true;
      } else if (this.project.startDate.setHours(0,0,0,0) >= this.project.endDate.setHours(0,0,0,0)) {
        this.dateSwitch = true;
      } else {
        this.activeModal.close(this.project);
        if (this.project.devTeam)
          this.project.devTeam = this.devTeams.find(i => i.name == this.project.devTeam.toString());
        else
          this.project.devTeam = this.devTeams[0];
        this.project.startDate = new Date();
        this.project.endDate = new Date();
        console.log(this.project.productBuyer);
        console.log(this.project.devTeam);
        this.apiService.project.put(this.project, true).subscribe(value =>
          console.log(this.project)
        );
      }
    }
  }

  toDateFormat(dateObject) {
    return new Date(dateObject.year, dateObject.month-1, dateObject.day);
  }
}
