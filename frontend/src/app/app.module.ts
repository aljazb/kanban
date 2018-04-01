import {APP_INITIALIZER, NgModule, NO_ERRORS_SCHEMA} from '@angular/core';
import { BrowserModule }  from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { HttpClientModule }    from '@angular/common/http';

import { KeycloakService, KeycloakAngularModule } from 'keycloak-angular';
import { KeycloakInitializer } from './api/keycloak/keycloak-init';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppRoutingModule }     from './app-routing.module';

import { ApiService } from './api/api.service';
import { KeycloakAuthGuardService } from './api/keycloak/keycloak-auth-guard.service';

import { AppComponent }         from './app.component';
import { DashboardComponent }   from './view/route/dashboard/dashboard.component';
import { HeaderComponent }   from './view/base/header/header.component';
import { FooterComponent } from './view/base/footer/footer.component';
import { DevTeamComponent } from './view/route/dev-team/dev-team.component';
import { ProjectComponent } from './view/route/project/project.component';
import { BoardComponent } from './view/route/board/board.component';
import { AdminComponent } from './view/route/admin/admin.component';
import { UserPagingComponent } from './view/components/paging/user-paging/user-paging.component';
import { UserDetailsComponent } from './view/components/details/user-details/user-details.component';
import { ProjectFormComponent } from './view/components/forms/project-form/project-form.component';
import { DevTeamPagingComponent } from './view/components/paging/dev-team-paging/dev-team-paging.component';
import { ProjectPagingComponent } from './view/components/paging/project-paging/project-paging.component';
import { BoardPagingComponent } from './view/components/paging/board-paging/board-paging.component';
import { ProfileComponent } from './view/route/profile/profile.component';
import {LoginService} from './api/login.service';
import { UserSelectionFormComponent } from './view/components/forms/user-selection-form/user-selection-form.component';
import { UserAccountFormComponent } from './view/components/forms/user-account-form/user-account-form.component';
import { ProjectDeleteConfirmationComponent } from './view/components/forms/project-delete-confirmation/project-delete-confirmation.component';


@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    HttpClientModule,
    KeycloakAngularModule,
    NgbModule.forRoot()
  ],
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    DashboardComponent,
    DevTeamComponent,
    ProjectComponent,
    BoardComponent,
    AdminComponent,
    UserPagingComponent,
    UserDetailsComponent,
    DevTeamPagingComponent,
    ProjectPagingComponent,
    BoardPagingComponent,
    ProfileComponent,
    ProjectFormComponent,
    UserAccountFormComponent,
    ProjectDeleteConfirmationComponent,
    UserSelectionFormComponent,
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: KeycloakInitializer,
      multi: true,
      deps: [KeycloakService]
    },
    KeycloakAuthGuardService,
    ApiService,
    LoginService,
  ],
  bootstrap: [ AppComponent ],
  entryComponents: [
    ProjectFormComponent,
    UserSelectionFormComponent,
    ProjectFormComponent,
    ProjectDeleteConfirmationComponent,
    UserAccountFormComponent
  ],
  schemas: [ NO_ERRORS_SCHEMA ]
})
export class AppModule { }
