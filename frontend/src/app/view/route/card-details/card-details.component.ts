import { Component, OnInit } from '@angular/core';
import {ApiService} from '../../../api/services/api.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Card} from '../../../api/models/Card';
import {CardMove} from '../../../api/models/card-move';
import {CardFormComponent} from '../../components/forms/card-form/card-form.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ToasterService} from 'angular5-toaster/dist';
import {Membership} from '../../../api/models/Membership';
import {Board} from '../../../api/models/Board';
import {CardDeleteConfirmationComponent} from '../../components/forms/card-delete-confirmation/card-delete-confirmation.component';
import {SubtaskFormComponent} from '../../components/forms/subtask-form/subtask-form.component';
import {SubTask} from '../../../api/models/sub-task';
import {ProjectDeleteConfirmationComponent} from '../../components/forms/project-delete-confirmation/project-delete-confirmation.component';
@Component({
  selector: 'app-card-details',
  templateUrl: './card-details.component.html',
  styleUrls: ['./card-details.component.css']
})
export class CardDetailsComponent implements OnInit {

  id: string;
  card: Card;
  moves: CardMove[] = null;
  editEnabled: boolean = false;
  deleteEnabled: boolean = false;
  isAuthUserKanbanMaster: boolean = false;
  isAuthUserDeveloper: boolean = false;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private api: ApiService,
              private apiService:ApiService,
              private modalService: NgbModal,
              protected toaster: ToasterService) { }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.onInit();
  }

  private onInit(): void {
    this.api.card.get(this.id).subscribe(card => {
      this.card = card;

      if (card.cardMoves) {
        this.moves = card.cardMoves.sort((a, b) => b.createdOn - a.createdOn);
      }

      if (card.subTasks) {
        card.subTasks = card.subTasks.sort((a, b) => a.createdOn - b.createdOn)
      }

      this.api.project.get(this.card.project.id).subscribe(project => {
        this.isAuthUserKanbanMaster = Membership.isDeveloper(project.membership);
        this.isAuthUserDeveloper = Membership.isKanbanMaster(project.membership);
      });

      this.checkEdit();
    });
  }

  checkEdit() {
    let b: Board = this.card.boardPart.board;
    let m: Membership = this.card.membership;

    if (m != null) {
      let leafIndex = this.card.boardPart.leafNumber;

      if(Membership.isKanbanMaster(m)) {
        this.deleteEnabled = true;
        if(leafIndex < b.acceptanceTesting) {
          this.editEnabled = true;

        }
      } else if(Membership.isDeveloper(m)) {
        if(b.startDev <= leafIndex && leafIndex <= b.endDev) {
          this.editEnabled = true;
        }
      } else if(Membership.isProductOwner(m)) {
        if(leafIndex <= b.highestPriority) {
          this.editEnabled = true;
          this.deleteEnabled = true
        }
      }
    }
  }


  openCardEditModal() {
    const modalRef = this.modalService.open(CardFormComponent);
    (<CardFormComponent> modalRef.componentInstance).setCard(this.card);

    modalRef.result
      .then(value =>
        this.apiService.card.put(value, true).subscribe(value => {
          this.onInit();
        }, error2 => {
          this.toaster.pop("error", "Error updating card");
        }), reason => {});
  }

  openCreateSubtaskModal() {
    const modalRef = this.modalService.open(SubtaskFormComponent);
    (<SubtaskFormComponent> modalRef.componentInstance).initSubtaskCreation(this.card);

    modalRef.result
      .then(value =>
        this.apiService.subTask.post(value, true).subscribe(value => {
          console.log(value);
          location.reload();
        }, error2 => {
          this.toaster.pop("error", "Error creating subtask");
        }), reason => {});
  }

  updateCompleted(subtask: SubTask) {
    subtask.completed = !subtask.completed;

    this.apiService.subTask.put(subtask, true).subscribe(value => {
      console.log(value)
    }, error2 => {
      this.toaster.pop("error", "Error updating subtask");
    });
  }

  openEditSubtaskModal(subtask: SubTask) {
    const modalRef = this.modalService.open(SubtaskFormComponent);
    (<SubtaskFormComponent> modalRef.componentInstance).setSubtask(subtask, this.card);

    modalRef.result
      .then(value =>
        this.apiService.subTask.put(value, true).subscribe(value => {
          console.log(value)
        }, error2 => {
          this.toaster.pop("error", "Error updating subtask");
        }), reason => {});
  }

  openSubtaskDeleteConfirmationModal(subtask: SubTask) {
    const modalRef = this.modalService.open(ProjectDeleteConfirmationComponent);

    modalRef.result
      .then(value => {
        this.apiService.subTask.delete(subtask.id, true).subscribe(value => {
          this.toaster.pop("success", "Sub-task was deleted");
          location.reload();
        }, error2 => {
          this.toaster.pop("error", "Error deleting sub-task");
        });
      });
  }

  openDeleteConfirmationModal() {
    const modalRef = this.modalService.open(CardDeleteConfirmationComponent);

    modalRef.result
      .then(value => {
        this.apiService.card.deleteWithMessage(this.id, value, true).subscribe(value => {
          this.toaster.pop("success", "Card was deleted");
          this.router.navigate([`/project`, this.card.project.id]);
        }, error2 => {
          this.toaster.pop("error", "Error deleting Card");
        });
      });
  }
}
