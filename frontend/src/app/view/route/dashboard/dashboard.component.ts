import { Component, OnInit } from '@angular/core';
import {ApiService} from '../../../api/Api';
import {KeycloakService} from 'keycloak-angular/index';
import {UserAccount} from '../../../api/models/UserAccount';
import {Project} from '../../../api/models/Project';
import {QueryBuilder} from '../../../api/query/query-builder';
import {HttpParams} from '@angular/common/http';
import {ROLE_ADMINISTRATOR, ROLE_KANBAN_MASTER} from '../../../api/keycloak/keycloak-init';
import {ModalDismissReasons, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ProjectCreationFormComponent} from '../../components/forms/project-creation-form/project-creation-form.component';
import {DevTeam} from '../../../api/models/DevTeam';
import {Board} from '../../../api/models/Board';
import {Router} from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: [ './dashboard.component.css' ]
})
export class DashboardComponent implements OnInit {

  constructor(
    private router: Router,
    private keycloak:KeycloakService,
    private modalService: NgbModal) { }

  ngOnInit() {

  }

  openProjectCreateModal() {
    const modalRef = this.modalService.open(ProjectCreationFormComponent);
    modalRef.result
      .then(value => console.log(value))
      .catch(reason => console.log(reason));
  }

  goToProject(project: Project): void {
    console.log(project);
    this.router.navigate(['/project/' + project.id]);
  }

  goToDevTeam(devTeam: DevTeam): void {
    console.log(devTeam);
    this.router.navigate(['/dev-team/' + devTeam.id]);
  }

  goToBoard(board: Board): void {
    console.log(board);
    this.router.navigate(['/board/' + board.id]);
  }
}
