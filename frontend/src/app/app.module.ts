import {APP_INITIALIZER, NgModule, NO_ERRORS_SCHEMA} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { KeycloakService, KeycloakAngularModule } from 'keycloak-angular';
import { KeycloakInitializer } from './api/keycloak/keycloak-init';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppRoutingModule } from './app-routing.module';

import { KeycloakAuthGuardService } from './api/keycloak/keycloak-auth-guard.service';

import { AppComponent } from './app.component';
import { DashboardComponent } from './view/route/dashboard/dashboard.component';
import { HeaderComponent } from './view/base/header/header.component';
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
import { UserSelectionFormComponent } from './view/components/forms/user-selection-form/user-selection-form.component';
import { UserAccountFormComponent } from './view/components/forms/user-account-form/user-account-form.component';
import { ProjectDeleteConfirmationComponent } from './view/components/forms/project-delete-confirmation/project-delete-confirmation.component';
import { DevTeamFormComponent } from './view/components/forms/dev-team-form/dev-team-form.component';
import {ToasterModule} from 'angular5-toaster/dist';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { UserAccountPasswordFormComponent } from './view/components/forms/user-account-password-form/user-account-password-form.component';
import { DevTeamDetailsComponent } from './view/route/dev-team-details/dev-team-details.component';
import { ProjectDetailsComponent } from './view/route/project-details/project-details.component';
import { BoardDetailsComponent } from './view/route/board-details/board-details.component';
import { BoardPartFormComponent } from './view/components/forms/board-form/board-part-form/board-part-form.component';
import { BoardBaseFormComponent } from './view/components/forms/board-form/board-base-form/board-base-form.component';
import { BoardEditComponent } from './view/route/board-edit/board-edit.component';
import { CardDetailsComponent } from './view/route/card-details/card-details.component';
import { BoardDetailsEditComponent } from './view/route/board-details-edit/board-details-edit.component';
import {CardFormComponent} from './view/components/forms/card-form/card-form.component';
import {ApiService} from './api/services/api.service';
import {LoginService} from './api/services/login.service';
import {LocalBoardsService} from './services/local-boards/local-boards.service';
import { CardMoveConfirmationComponent } from './view/components/forms/card-move-confirmation/card-move-confirmation.component';
import { CardMoveBackConfirmationComponent } from './view/components/forms/card-move-back-confirmation/card-move-back-confirmation.component';
import { ColumnWipViolationConfirmationComponent } from './view/components/forms/column-wip-violation-confirmation/column-wip-violation-confirmation.component';
import { CardMovesPagingComponent } from './view/components/paging/card-moves-paging/card-moves-paging.component';
import { AnalysisComponent } from './view/route/analysis/analysis.component';
import {NgxChartsModule} from '@swimlane/ngx-charts';
import {AnalysisQueryComponent} from './view/components/forms/analysis-query/analysis-query.component';
import {AnalysisWorkflowComponent} from './view/components/forms/analysis-workflow/analysis-workflow.component';
import { AnalysisWipComponent } from './view/components/forms/analysis-wip/analysis-wip.component';
import { AnalysisTimeComponent } from './view/components/forms/analysis-time/analysis-time.component';
import { CardDeleteConfirmationComponent } from './view/components/forms/card-delete-confirmation/card-delete-confirmation.component';
import { CardMoveRulesPagingComponent } from './view/components/paging/card-move-rules-paging/card-move-rules-paging.component';
import {BoardSettingsComponent} from './view/route/board-settings/board-settings.component';
import {SubtaskFormComponent} from './view/components/forms/subtask-form/subtask-form.component';


@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    HttpClientModule,
    KeycloakAngularModule,
    ToasterModule,
    NgxChartsModule,
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
    CardFormComponent,
    SubtaskFormComponent,
    UserAccountFormComponent,
    ProjectDeleteConfirmationComponent,
    UserSelectionFormComponent,
    DevTeamFormComponent,
    UserAccountPasswordFormComponent,
    DevTeamDetailsComponent,
    ProjectDetailsComponent,
    BoardDetailsComponent,
    BoardPartFormComponent,
    BoardBaseFormComponent,
    BoardEditComponent,
    CardDetailsComponent,
    BoardDetailsEditComponent,
    CardMoveConfirmationComponent,
    CardMoveBackConfirmationComponent,
    ColumnWipViolationConfirmationComponent,
    CardMovesPagingComponent,
    AnalysisComponent,
    AnalysisQueryComponent,
    AnalysisWorkflowComponent,
    AnalysisWipComponent,
    AnalysisTimeComponent,
    CardDeleteConfirmationComponent,
    BoardSettingsComponent,
    CardMoveRulesPagingComponent
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
    LocalBoardsService
  ],
  bootstrap: [ AppComponent ],
  entryComponents: [
    ProjectFormComponent,
    CardFormComponent,
    SubtaskFormComponent,
    UserSelectionFormComponent,
    UserAccountPasswordFormComponent,
    ProjectFormComponent,
    ProjectDeleteConfirmationComponent,
    CardDeleteConfirmationComponent,
    UserAccountFormComponent,
    DevTeamFormComponent,
    CardMoveConfirmationComponent,
    CardMoveBackConfirmationComponent,
    ColumnWipViolationConfirmationComponent
  ],
  schemas: [ NO_ERRORS_SCHEMA ]
})
export class AppModule { }
