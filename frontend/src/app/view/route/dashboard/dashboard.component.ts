import { Component, OnInit } from '@angular/core';
import {ApiService} from '../../../api/Api';
import {KeycloakService} from 'keycloak-angular/index';
import {UserAccount} from '../../../api/models/UserAccount';
import {Project} from '../../../api/models/Project';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: [ './dashboard.component.scss' ]
})
export class DashboardComponent implements OnInit {

  projects: Project[];

  constructor(
    private apiService:ApiService) { }

  ngOnInit() {
    this.getProjects()
  }


  getProjects(): void {
    console.log("Getting projects");
    this.apiService.project.getList()
      .subscribe(projects => {
        this.projects = projects;
        console.log("Loaded projects");
        console.log(projects);
      });
  }
}
