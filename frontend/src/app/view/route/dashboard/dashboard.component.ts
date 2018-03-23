import { Component, OnInit } from '@angular/core';
import {ApiService} from '../../../api/Api';
import {KeycloakService} from 'keycloak-angular/index';
import {UserAccount} from '../../../api/models/UserAccount';
import {Project} from '../../../api/models/Project';
import {QueryBuilder} from '../../../api/query/query-builder';
import {HttpParams} from '@angular/common/http';

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
    if(this.keycloak.getKeycloakInstance().authenticated){
      this.loadContent();
    }
  }

  loadContent(): void {
    this.getProjects();
  }

  getProjects(): void {
    console.log("Getting projects");

    let p: HttpParams = QueryBuilder.query().eq("name", "ime").build();

    this.apiService.project.getList(p)
      .subscribe(projects => {
        this.projects = projects;
        console.log("Loaded projects");
        console.log(projects);
      });
  }
}
