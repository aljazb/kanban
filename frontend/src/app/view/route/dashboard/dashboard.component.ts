import { Component, OnInit } from '@angular/core';
import {ApiService} from '../../../api/Api';
import {KeycloakService} from 'keycloak-angular/index';
import {UserAccount} from '../../../api/models/UserAccount';
import {Project} from '../../../api/models/Project';
import {QueryBuilder} from '../../../api/query/query-builder';
import {HttpParams} from '@angular/common/http';
import {ROLE_ADMINISTRATOR} from '../../../api/keycloak/keycloak-init';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: [ './dashboard.component.scss' ]
})
export class DashboardComponent implements OnInit {


  projects: Project[];

  constructor(
    private keycloak:KeycloakService,
    private apiService:ApiService) { }

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
  }

  getProjects(): void {
    console.log("Getting projects");

    this.apiService.project.getList()
      .subscribe(projects => {
        this.projects = projects.items;
        console.log("Loaded projects");
        console.log(projects);
      });
  }
}
