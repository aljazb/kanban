import { Component, OnInit } from '@angular/core';
import {QueryBuilder} from '../../../api/query/query-builder';
import {HttpParams} from '@angular/common/http';
import {ROLE_KANBAN_MASTER} from '../../../api/keycloak/keycloak-init';
import {KeycloakService} from 'keycloak-angular/index';
import {ApiService} from '../../../api/Api';
import {DevTeam} from '../../../api/models/DevTeam';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})
export class ProjectComponent implements OnInit {
  startDate;
  endDate;
  devTeams: DevTeam[];

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
    this.getDevTeams();
  }

  getDevTeams(): void {
    console.log("Getting dev teams");

    this.apiService.devTeam.getList()
      .subscribe(devTeams => {
        this.devTeams = devTeams.items;
        console.log("Loaded dev teams");
        console.log(devTeams);
      });
  }
}
