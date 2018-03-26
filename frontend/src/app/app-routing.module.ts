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

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'admin', component: AdminComponent, data: {roles: {ROLE_ADMINISTRATOR}} },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'dev-team/:id', component: DevTeamComponent },
  { path: 'project/:id', component: ProjectComponent, data: {roles: {ROLE_KANBAN_MASTER}} },
  { path: 'board/:id', component: BoardComponent },
  { path: 'profile', component: ProfileComponent },
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
