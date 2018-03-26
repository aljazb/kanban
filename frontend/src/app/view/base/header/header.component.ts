import { Component, OnInit } from '@angular/core';

import { KeycloakService } from 'keycloak-angular';
import {UserAccount} from '../../../api/models/UserAccount';
import {ApiService} from '../../../api/Api';
import {environment} from '../../../../environments/environment';
import {ROLE_ADMINISTRATOR} from '../../../api/keycloak/keycloak-init';
import {Router} from '@angular/router';
import {KeycloakAuthGuardService} from '../../../api/keycloak/keycloak-auth-guard.service';
import {LoginService} from '../../../api/login.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  navbarCollapsed = true;

  keycloakUrl: string = environment.authHostname + "/auth";
  isAdministrator: boolean = false;

  constructor(
    public keycloak: KeycloakAuthGuardService,
    public loginService: LoginService) { }

  ngOnInit() {
    this.keycloak.isLoggedIn()
      .then(isLoggedIn => {
        if(isLoggedIn){
          this.isAdministrator = this.keycloak.isUserInRole(ROLE_ADMINISTRATOR);
          this.loginService.loginApi();
        }
      })
  }

  login(): void {
    this.keycloak.login();
  }

  logout(): void {
    this.keycloak.logout();
  }

}
