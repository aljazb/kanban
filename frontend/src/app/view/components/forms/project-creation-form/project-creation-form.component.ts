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
}
