import { Component, OnInit } from '@angular/core';

import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-app-header',
  templateUrl: './app-header.component.html',
  styleUrls: ['./app-header.component.css']
})
export class AppHeaderComponent implements OnInit {

  public isLoggedIn: boolean = false;

  constructor(public keycloak: KeycloakService) { }

  ngOnInit() {
    this.keycloak.isLoggedIn()
      .then(isLoggedIn => this.isLoggedIn = isLoggedIn)
  }

  handleAuth(){
    if(this.isLoggedIn){
      this.keycloak.logout();
    } else {
      this.keycloak.login();
    }
  }

}
