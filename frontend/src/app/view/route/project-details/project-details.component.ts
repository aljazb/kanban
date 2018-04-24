import { Component, OnInit } from '@angular/core';
import {ProjectDeleteConfirmationComponent} from '../../components/forms/project-delete-confirmation/project-delete-confirmation.component';
import {ProjectFormComponent} from '../../components/forms/project-form/project-form.component';
import {ActivatedRoute, Router} from '@angular/router';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ApiService} from '../../../api/services/api.service';
import {Project} from '../../../api/models/Project';
import {CardFormComponent} from '../../components/forms/card-form/card-form.component';
import {Membership} from '../../../api/models/Membership';

@Component({
  selector: 'app-project-details',
  templateUrl: './project-details.component.html',
  styleUrls: ['./project-details.component.css']
})
export class ProjectDetailsComponent implements OnInit {

  id: string;
  project: Project;
  isAuthUserKanbanMaster: boolean;
  isAuthUserProductOwner: boolean;
  cardsAssigned: boolean = false;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private apiService:ApiService,
              private modalService: NgbModal) { }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.getProject();
  }

  getProject() {
    this.apiService.project.get(this.id).subscribe(project => {
      this.project = project;
      this.apiService.devTeam.get(project.devTeam.id).subscribe(devTeam => project.devTeam = devTeam);

      this.isAuthUserKanbanMaster = Membership.isKanbanMaster(this.project.membership);
      this.isAuthUserProductOwner = Membership.isProductOwner(this.project.membership);
    });
  }


  openProjectCreateModal() {
    const modalRef = this.modalService.open(ProjectFormComponent);
    (<ProjectFormComponent> modalRef.componentInstance).setInitialProject(this.project);
    (<ProjectFormComponent> modalRef.componentInstance).setStartDateState(this.cardsAssigned);

    modalRef.result
      .then(value =>
        this.apiService.project.put(value, true).subscribe(value =>
          console.log(value)
        ))
      .catch(reason => console.log(reason));
  }

  openCardCreateModal() {
    const modalRef = this.modalService.open(CardFormComponent);
    (<CardFormComponent> modalRef.componentInstance).setProject(this.project);

    modalRef.result
      .then(value =>
        this.apiService.card.post(value, true).subscribe(value =>
          console.log(value)
        ))
      .catch(reason => console.log(reason));
  }

  openDeleteConfirmationModal() {
    const modalRef = this.modalService.open(ProjectDeleteConfirmationComponent);

    modalRef.result
      .then(value => this.deleteProject())
      .catch(reason => console.log(reason));
  }

  deleteProject() {
    this.apiService.project.delete(this.id, true).subscribe(value =>
        this.router.navigate([`/project`])
    );

  }

}
