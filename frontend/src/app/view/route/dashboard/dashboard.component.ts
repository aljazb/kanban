import { Component, OnInit } from '@angular/core';
import { Hero } from '../../../demo/hero';
import { HeroService } from '../../../demo/hero.service';
import {ApiService} from '../../../api/Api';
import {KeycloakService} from 'keycloak-angular/index';
import {UserAccount} from '../../../api/models/UserAccount';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: [ './dashboard.component.css' ]
})
export class DashboardComponent implements OnInit {
  heroes: Hero[] = [];

  private user: UserAccount;

  constructor(
    private heroService: HeroService,
    private keycloakService:KeycloakService,
    private apiService:ApiService) { }

  ngOnInit() {
    this.getHeroes();

    /*if(this.keycloakService.getKeycloakInstance().authenticated){
      this.apiService.userAccount.login().subscribe(user => this.user = user);
    }*/

  }

  getHeroes(): void {
    this.heroService.getHeroes()
      .subscribe(heroes => this.heroes = heroes.slice(1, 5));
  }
}
