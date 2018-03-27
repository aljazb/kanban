import { Component, OnInit } from '@angular/core';
import {ApiService} from '../../../api/api.service';
import {ActivatedRoute} from '@angular/router';
import {Project} from '../../../api/models/Project';
import {ProjectFormComponent} from '../../components/forms/project-form/project-form.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';


@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})
export class ProjectComponent implements OnInit {

  id: string;
  project: Project;

  constructor( private route: ActivatedRoute,
               private apiService:ApiService,
               private modalService: NgbModal) { }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.getProject();
  }

  getProject() {
    this.apiService.project.get(this.id).subscribe(project =>
      this.assignProjectAndDevTeam(project)
    );
  }

  assignProjectAndDevTeam(project) {
    this.project = project;
    this.apiService.devTeam.get(project.devTeam.id).subscribe(devTeam => project.devTeam = devTeam);
  }

  openProjectCreateModal() {
    const modalRef = this.modalService.open(ProjectFormComponent);
    (<ProjectFormComponent> modalRef.componentInstance).setInitialProject(this.project);

    modalRef.result
      .then(value => console.log(value))
      .catch(reason => console.log(reason));
  }
}
