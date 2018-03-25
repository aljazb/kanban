import {Component, Input} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {DevTeam} from '../../../../api/models/DevTeam';
import {Project} from '../../../../api/models/Project';
import {ApiService} from '../../../../api/Api';

@Component({
  selector: 'app-project-creation-form',
  templateUrl: './project-creation-form.component.html',
  styleUrls: ['./project-creation-form.component.css']
})
export class ProjectCreationFormComponent {

  devTeams: DevTeam[];
  project: Project = new Project();
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

  createProject() {
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
        this.apiService.project.post(this.project, true).subscribe(value =>
          console.log(this.project)
        );
      }
    }
  }

  toDateFormat(dateObject) {
    return new Date(dateObject.year, dateObject.month-1, dateObject.day);
  }
}
