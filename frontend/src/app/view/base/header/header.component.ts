import { Component, OnInit } from '@angular/core';

import { KeycloakService } from 'keycloak-angular';
import {UserAccount} from '../../../api/models/UserAccount';
import {ApiService} from '../../../api/Api';
import {environment} from '../../../../environments/environment';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  user: UserAccount;
  keycloakUrl: string = environment.authHostname + "/auth";

  constructor(
    public keycloak: KeycloakService,
    private apiService:ApiService) { }

  ngOnInit() {
    this.keycloak.isLoggedIn()
      .then(isLoggedIn => {
        if(isLoggedIn){
          this.loginApi();
        }
      })
  }

  login(){
    this.keycloak.login();
  }

  logout(){
    this.keycloak.logout();
  }

  loginApi(): void {
    this.apiService.userAccount.login().subscribe(user => this.user = user);
  }

}
