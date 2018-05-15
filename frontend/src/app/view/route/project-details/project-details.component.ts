import { Component, OnInit } from '@angular/core';
import {ProjectDeleteConfirmationComponent} from '../../components/forms/project-delete-confirmation/project-delete-confirmation.component';
import {ProjectFormComponent} from '../../components/forms/project-form/project-form.component';
import {ActivatedRoute, Router} from '@angular/router';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ApiService} from '../../../api/services/api.service';
import {Project} from '../../../api/models/Project';
import {CardFormComponent} from '../../components/forms/card-form/card-form.component';
import {Membership} from '../../../api/models/Membership';
import {ToasterService} from 'angular5-toaster/dist';
import {LoginService} from '../../../api/services/login.service';
import {Board} from '../../../api/models/Board';

@Component({
  selector: 'app-project-details',
  templateUrl: './project-details.component.html',
  styleUrls: ['./project-details.component.css']
})
export class ProjectDetailsComponent implements OnInit {

  id: string;
  project: Project;
  isAuthUserOwner: boolean = false;
  isAuthUserKanbanMaster: boolean;
  isAuthUserProductOwner: boolean;
  cardsAssigned: boolean = false;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private apiService:ApiService,
              private login: LoginService,
              private modalService: NgbModal,
              protected toaster: ToasterService) { }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.onInit();
  }

  private onInit(): void {
    this.login.getUser().subscribe(authUser => {
      this.apiService.project.get(this.id).subscribe(project => {
        this.project = project;
        this.apiService.devTeam.get(project.devTeam.id).subscribe(devTeam => project.devTeam = devTeam);

        this.isAuthUserKanbanMaster = Membership.isKanbanMaster(this.project.membership);
        this.isAuthUserProductOwner = Membership.isProductOwner(this.project.membership);
        this.isAuthUserOwner = authUser.id == this.project.owner.id;

        if(Array.isArray(this.project.cards)) {
          this.cardsAssigned = this.project.cards.length > 0;
        }
      });
    })
  }

  openProjectCreateModal() {
    const modalRef = this.modalService.open(ProjectFormComponent);
    (<ProjectFormComponent> modalRef.componentInstance).setInitialProject(this.project);
    (<ProjectFormComponent> modalRef.componentInstance).setStartDateState(this.cardsAssigned);

    modalRef.result
      .then(value =>
        this.apiService.project.put(value, true).subscribe(value => {
          this.toaster.pop("success", "Project was updated");
          this.onInit();
        }, error2 => {
          this.toaster.pop("error", "Error updating project")
        }));
  }

  openCardCreateModal() {
    const modalRef = this.modalService.open(CardFormComponent);
    (<CardFormComponent> modalRef.componentInstance).initCardCreation(this.project);


    modalRef.result
      .then(value =>
        this.apiService.card.post(value, true).subscribe(value => {
          this.toaster.pop("success", "Card was updated");
          this.router.navigate(['/card', value.id])
          this.onInit();
        }, error2 => {
          this.toaster.pop("error", "Error creating card");
        }), reason => {});
  }

  openDeleteConfirmationModal() {
    const modalRef = this.modalService.open(ProjectDeleteConfirmationComponent);

    modalRef.result
      .then(value => {
        this.apiService.project.delete(this.id, true).subscribe(value => {
            this.toaster.pop("success", "Project was deleted");
            this.router.navigate([`/project`]);
        }, error2 => {
          this.toaster.pop("error", "Error deleting project");
        });
      });
  }


}
