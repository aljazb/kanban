import { Component, OnInit } from '@angular/core';
import {ApiService} from '../../../api/api.service';
import {ActivatedRoute} from '@angular/router';
import {Project} from '../../../api/models/Project';
import {ProjectFormComponent} from '../../components/forms/project-form/project-form.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ProjectDeleteConfirmationComponent} from '../../components/forms/project-delete-confirmation/project-delete-confirmation.component';
import {ROLE_KANBAN_MASTER} from '../../../api/keycloak/keycloak-init';
import {KeycloakService} from 'keycloak-angular/index';


@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})
export class ProjectComponent implements OnInit {

  id: string;
  project: Project;
  isKanbanMaster: boolean;
  cardsAssigned: boolean = false;

  constructor( private route: ActivatedRoute,
               private apiService:ApiService,
               private keycloak:KeycloakService,
               private modalService: NgbModal) { }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.getProject();
    this.checkStatus();
  }

  checkStatus() {
    this.keycloak.isLoggedIn()
      .then(isLoggedIn => {
        if(isLoggedIn){
          this.isKanbanMaster = this.keycloak.isUserInRole(ROLE_KANBAN_MASTER);
        }
      });
  }


  getProject() {
    this.apiService.project.get(this.id).subscribe(project =>
      this.assignProjectAndDevTeam(project)
    );
  }

  assignProjectAndDevTeam(project) {
    this.project = project;
    this.apiService.devTeam.get(project.devTeam.id).subscribe(devTeam => project.devTeam = devTeam);
  }

  openProjectCreateModal() {
    const modalRef = this.modalService.open(ProjectFormComponent);
    (<ProjectFormComponent> modalRef.componentInstance).setInitialProject(this.project);
    (<ProjectFormComponent> modalRef.componentInstance).setStartDateState(this.cardsAssigned);

    modalRef.result
      .then(value =>
        this.apiService.project.put(value, true).subscribe(value =>
          console.log(value)
        ))
      .catch(reason => console.log(reason));
  }

  openDeleteConfirmationModal() {
    const modalRef = this.modalService.open(ProjectDeleteConfirmationComponent);

    modalRef.result
      .then(value => this.deleteProject())
      .catch(reason => console.log(reason));
  }

  deleteProject() {
    this.apiService.project.delete(this.id, true).subscribe(value =>
      console.log(value)
    );

  }
}
