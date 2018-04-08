import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DashboardComponent }   from './view/route/dashboard/dashboard.component';
import {DevTeamComponent} from './view/route/dev-team/dev-team.component';
import {ProjectComponent} from './view/route/project/project.component';
import {BoardComponent} from './view/route/board/board.component';
import {AdminComponent} from './view/route/admin/admin.component';
import {ROLE_ADMINISTRATOR} from './api/keycloak/keycloak-init';
import {ROLE_KANBAN_MASTER} from './api/keycloak/keycloak-init';
import {ProfileComponent} from './view/route/profile/profile.component';
import {DevTeamDetailsComponent} from './view/route/dev-team-details/dev-team-details.component';
import {ProjectDetailsComponent} from './view/route/project-details/project-details.component';
import {BoardDetailsComponent} from './view/route/board-details/board-details.component';
import {BoardEditComponent} from './view/route/board-edit/board-edit.component';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'admin', component: AdminComponent, data: {roles: {ROLE_ADMINISTRATOR}} },

  { path: 'dev-team', component: DevTeamComponent },
  { path: 'dev-team/:id', component: DevTeamDetailsComponent },

  { path: 'project', component: ProjectComponent },
  { path: 'project/:id', component: ProjectDetailsComponent },

  { path: 'board', component: BoardComponent },
  { path: 'board/:id', component: BoardDetailsComponent },
  { path: 'board-edit', component: BoardEditComponent },
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
