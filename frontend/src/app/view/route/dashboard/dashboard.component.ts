import {Component, OnInit} from '@angular/core';
import {KeycloakService} from 'keycloak-angular/index';
import {Project} from '../../../api/models/Project';
import { NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ProjectFormComponent} from '../../components/forms/project-form/project-form.component';
import {DevTeam} from '../../../api/models/DevTeam';
import {Board} from '../../../api/models/Board';
import {Router} from '@angular/router';
import {LoginService} from '../../../api/login.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: [ './dashboard.component.css' ]
})
export class DashboardComponent implements OnInit {

  isLoggedIn: boolean;

  constructor(
    private router: Router,
    private loginService: LoginService,
    private modalService: NgbModal) { }

  ngOnInit() {
    this.loginService.isLoggedIn()
      .subscribe(isLoggedIn => this.isLoggedIn = isLoggedIn);
  }

  openProjectCreateModal() {
    const modalRef = this.modalService.open(ProjectFormComponent);
    (<ProjectFormComponent> modalRef.componentInstance).setInitialProject(new Project());
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
