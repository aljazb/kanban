import {Component, OnInit} from '@angular/core';
import {KeycloakService} from 'keycloak-angular/index';
import {Project} from '../../../api/models/Project';
import { NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ProjectFormComponent} from '../../components/forms/project-form/project-form.component';
import {DevTeam} from '../../../api/models/DevTeam';
import {Board} from '../../../api/models/Board';
import {Router} from '@angular/router';
import {ApiService} from '../../../api/api.service';
import {ROLE_ADMINISTRATOR, ROLE_KANBAN_MASTER} from '../../../api/keycloak/keycloak-init';
import {LoginService} from '../../../api/login.service';
import {DevTeamFormComponent} from '../../components/forms/dev-team-form/dev-team-form.component';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: [ './dashboard.component.css' ]
})
export class DashboardComponent implements OnInit {

  isLoggedIn: boolean = false;
  isKanbanMaster: boolean = false;

  constructor(
    private router: Router,
    private apiService:ApiService,
    private modalService: NgbModal,
    public loginService: LoginService) { }
  login(): void {
    this.loginService.login();
  }
  ngOnInit() {
    this.loginService.getUser().subscribe(user => {
      if(user != null) {
        this.isLoggedIn = true;
        this.isKanbanMaster = user.inRoleKanbanMaster;
      }
    });
  }

  openDevTeamCreateModal() {
    const modalRef = this.modalService.open(DevTeamFormComponent);

    modalRef.result
      .then(value =>
        this.apiService.devTeam.post(value, true).subscribe(devTeam => {
          this.router.navigate([`/dev-team/${devTeam.id}`]);
        }))
      .catch(reason => console.log(reason));
  }

  openProjectCreateModal() {
    const modalRef = this.modalService.open(ProjectFormComponent);
    (<ProjectFormComponent> modalRef.componentInstance).setInitialProject(new Project());
    modalRef.result
      .then(value =>
        this.apiService.project.post(value, true).subscribe(value =>
          console.log(value)
        ))
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
