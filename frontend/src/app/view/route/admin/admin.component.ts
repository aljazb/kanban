import { Component, OnInit } from '@angular/core';
import {KeycloakAuthGuardService} from '../../../api/keycloak/keycloak-auth-guard.service';


@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {

  constructor(private keycloakGuard:KeycloakAuthGuardService) { }

  ngOnInit() {
    this.keycloakGuard.handleAuth();
  }

}
