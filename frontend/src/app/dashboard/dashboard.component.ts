import { Component, OnInit } from '@angular/core';
import { Hero } from '../hero';
import { HeroService } from '../hero.service';
import {ApiService} from '../api/Api';
import {KeycloakService} from 'keycloak-angular/index';
import {logging} from 'selenium-webdriver';
import LogManager = logging.LogManager;

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: [ './dashboard.component.css' ]
})
export class DashboardComponent implements OnInit {
  heroes: Hero[] = [];

  constructor(private heroService: HeroService, private keycloakService:KeycloakService, private apiService:ApiService) { }

  ngOnInit() {
    this.getHeroes();

    if(this.keycloakService.getKeycloakInstance().authenticated){
      //this.apiService.userAccount.login().subscribe(user => );
    }

  }

  getHeroes(): void {
    this.heroService.getHeroes()
      .subscribe(heroes => this.heroes = heroes.slice(1, 5));
  }
}
