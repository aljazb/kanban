import { Component, OnInit } from '@angular/core';
import {QueryBuilder} from '../../../api/query/query-builder';
import {HttpParams} from '@angular/common/http';
import {ROLE_KANBAN_MASTER} from '../../../api/keycloak/keycloak-init';
import {KeycloakService} from 'keycloak-angular/index';
import {ApiService} from '../../../api/Api';
import {DevTeam} from '../../../api/models/DevTeam';
import {ActivatedRoute} from '@angular/router';
import {Project} from '../../../api/models/Project';
import {ProjectCreationFormComponent} from '../../components/forms/project-creation-form/project-creation-form.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ProjectEditFormComponent} from '../../components/forms/project-edit-form/project-edit-form.component';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})
export class ProjectComponent implements OnInit {

  id: string;
  project: Project;

  constructor( private route: ActivatedRoute,
               private apiService:ApiService,
               private modalService: NgbModal) { }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.getProject();
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
    const modalRef = this.modalService.open(ProjectEditFormComponent);
    (<ProjectComponent>modalRef.componentInstance).project = Object.create(this.project);
    modalRef.result
      .then(value => console.log(value))
      .catch(reason => console.log(reason));
  }
}
