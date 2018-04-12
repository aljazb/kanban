import { Component, OnInit } from '@angular/core';
import {Project} from '../../../api/models/Project';
import {Router} from '@angular/router';
import {ProjectFormComponent} from '../../components/forms/project-form/project-form.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ApiService} from '../../../api/api.service';
import {LoginService} from '../../../api/login.service';


@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})
export class ProjectComponent implements OnInit {

  isKanbanMaster: boolean;

  constructor(private router: Router,
              private apiService:ApiService,
              private loginService: LoginService,
              private modalService: NgbModal) { }

  ngOnInit() {
    this.loginService.getUser().subscribe(user => {
      this.isKanbanMaster = user.inRoleKanbanMaster
    });
  }

  goToProject(project: Project): void {
    this.router.navigate(['/project/' + project.id]);
  }

  openProjectCreateModal() {
    const modalRef = this.modalService.open(ProjectFormComponent);
    modalRef.result
      .then(value =>
        this.apiService.project.post(value, true).subscribe(value =>
          this.router.navigate([`/project/${value.id}`])
        ))
      .catch(reason => console.log(reason));
  }
}
