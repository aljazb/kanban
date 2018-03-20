import { Component, OnInit } from '@angular/core';
import { Hero } from '../../../demo/hero';
import { HeroService } from '../../../demo/hero.service';
import {ApiService} from '../../../api/Api';
import {KeycloakService} from 'keycloak-angular/index';
import {UserAccount} from '../../../api/models/UserAccount';
import {Project} from '../../../api/models/Project';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: [ './dashboard.component.css' ]
})
export class DashboardComponent implements OnInit {
  heroes: Hero[] = [];

  user: UserAccount;
  projects: Project[];

  constructor(
    private heroService: HeroService,
    private keycloakService:KeycloakService,
    private apiService:ApiService) { }

  ngOnInit() {
    //this.getHeroes();
    this.login();
    this.getProjects()
  }

  getHeroes(): void {
    this.heroService.getHeroes()
      .subscribe(heroes => this.heroes = heroes.slice(1, 5));
  }

  getProjects(): void {
    console.log("Getting projects");
    this.apiService.project.getList()
      .subscribe(projects => {
        this.projects = projects;
        console.log("Loaded projects");
        console.log(projects);
      });
  }

  login(): void {
    console.log(this.keycloakService.getKeycloakInstance().authenticated);
    if(this.keycloakService.getKeycloakInstance().authenticated) {
      console.log("About to login");
      this.apiService.userAccount.login()
        .subscribe(user => {
          console.log("Logged in");
          this.user = user;
          console.log(user);
        });
    }
  }
}
