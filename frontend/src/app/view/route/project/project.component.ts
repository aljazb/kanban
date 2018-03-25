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

  constructor(
    private keycloak:KeycloakService,
    private apiService:ApiService) { }

  ngOnInit() {

  }


}
