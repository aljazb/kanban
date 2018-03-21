import { Component, OnInit } from '@angular/core';

import { KeycloakService } from 'keycloak-angular';
import {UserAccount} from '../../../api/models/UserAccount';
import {ApiService} from '../../../api/Api';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  user: UserAccount;
  isLoggedIn: boolean = false;

  constructor(
    public keycloak: KeycloakService,
    private apiService:ApiService) { }

  ngOnInit() {
    this.keycloak.isLoggedIn()
      .then(isLoggedIn => {
        this.isLoggedIn = isLoggedIn;
        this.loginApi();
      })
  }

  login(){
    this.keycloak.login();
  }

  logout(){
    this.keycloak.logout();
  }

  loginApi(): void {
    if(this.keycloak.getKeycloakInstance().authenticated) {
      console.log("About to login api");
      this.apiService.userAccount.login()
        .subscribe(user => {
          console.log("Logged in api");
          this.user = user;
          console.log(user);
        });
    }
  }

}
