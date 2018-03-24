import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DashboardComponent }   from './view/route/dashboard/dashboard.component';
import {DevTeamComponent} from './view/route/dev-team/dev-team.component';
import {ProjectComponent} from './view/route/project/project.component';
import {BoardComponent} from './view/route/board/board.component';
import {AdminComponent} from './view/route/admin/admin.component';
import {ROLE_ADMINISTRATOR} from './api/keycloak/keycloak-init';
import {ROLE_KANBAN_MASTER} from './api/keycloak/keycloak-init';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'dev-team', component: DevTeamComponent },
  { path: 'project', component: ProjectComponent, data: {roles: {ROLE_KANBAN_MASTER}} },
  { path: 'board', component: BoardComponent },
  { path: 'admin', component: AdminComponent, data: {roles: {ROLE_ADMINISTRATOR}} }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
