import {APP_INITIALIZER, NgModule, NO_ERRORS_SCHEMA} from '@angular/core';
import { BrowserModule }  from '@angular/platform-browser';
import { FormsModule }    from '@angular/forms';
import { HttpClientModule }    from '@angular/common/http';

import { KeycloakService, KeycloakAngularModule } from 'keycloak-angular';
import { KeycloakInitializer } from './api/keycloak/keycloak-init';


import { AppRoutingModule }     from './app-routing.module';
import { MDBBootstrapModule } from 'angular-bootstrap-md';

import { AppComponent }         from './app.component';
import { DashboardComponent }   from './view/route/dashboard/dashboard.component';
import { HeaderComponent }   from './view/base/header/header.component';
import {ApiService} from './api/Api';
import {KeycloakAuthGuardService} from './api/keycloak/keycloak-auth-guard.service';
import { FooterComponent } from './view/base/footer/footer.component';
import { DevTeamComponent } from './view/route/dev-team/dev-team.component';
import { ProjectComponent } from './view/route/project/project.component';
import { BoardComponent } from './view/route/board/board.component';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
    KeycloakAngularModule,
    MDBBootstrapModule.forRoot()
  ],
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    DashboardComponent,
    DevTeamComponent,
    ProjectComponent,
    BoardComponent
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: KeycloakInitializer,
      multi: true,
      deps: [KeycloakService]
    },
    KeycloakAuthGuardService,
    ApiService
  ],
  bootstrap: [ AppComponent ],
  schemas: [ NO_ERRORS_SCHEMA ]
})
export class AppModule { }
