import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DashboardComponent }   from './view/route/dashboard/dashboard.component';
import {DevTeamComponent} from './view/route/dev-team/dev-team.component';
import {ProjectComponent} from './view/route/project/project.component';
import {BoardComponent} from './view/route/board/board.component';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'dev-team', component: DevTeamComponent },
  { path: 'project', component: ProjectComponent },
  { path: 'board', component: BoardComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
