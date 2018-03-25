import { Component, OnInit } from '@angular/core';
import {QueryBuilder} from '../../../api/query/query-builder';
import {HttpParams} from '@angular/common/http';
import {ROLE_KANBAN_MASTER} from '../../../api/keycloak/keycloak-init';
import {KeycloakService} from 'keycloak-angular/index';
import {ApiService} from '../../../api/Api';
import {DevTeam} from '../../../api/models/DevTeam';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})
export class ProjectComponent implements OnInit {

  id: string;

  constructor( private route: ActivatedRoute) { }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
  }

}
