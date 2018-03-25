import { Component, OnInit } from '@angular/core';
import {ApiService} from '../../../api/Api';
import {KeycloakService} from 'keycloak-angular/index';
import {UserAccount} from '../../../api/models/UserAccount';
import {Project} from '../../../api/models/Project';
import {QueryBuilder} from '../../../api/query/query-builder';
import {HttpParams} from '@angular/common/http';
import {ROLE_ADMINISTRATOR, ROLE_KANBAN_MASTER} from '../../../api/keycloak/keycloak-init';
import {ModalDismissReasons, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ProjectCreationFormComponent} from '../../components/forms/project-creation-form/project-creation-form.component';
import {DevTeam} from '../../../api/models/DevTeam';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: [ './dashboard.component.css' ]
})
export class DashboardComponent implements OnInit {

  projects: Project[];
  devTeams: DevTeam[];

  constructor(
    private keycloak:KeycloakService,
    private apiService:ApiService,
    private modalService: NgbModal) { }

  ngOnInit() {
    this.keycloak.isLoggedIn()
      .then(isLoggedIn => {
        if(isLoggedIn){
          this.loadContent();
        }
      })
  }

  loadContent(): void {
    this.getProjects();
    this.getDevTeams();
  }

  getProjects(): void {
    this.apiService.project.getList()
      .subscribe(projects => this.projects = projects.items);
  }

  getDevTeams(): void {
    this.apiService.devTeam.getList()
      .subscribe(devTeams => this.devTeams = devTeams.items);
  }

  open() {
    const modalRef = this.modalService.open(ProjectCreationFormComponent);
    modalRef.componentInstance.devTeams = this.devTeams;
    modalRef.result.then(value => console.log(value));
  }
}
