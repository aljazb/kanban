import { Component, OnInit } from '@angular/core';
import {ApiService} from '../../../api/services/api.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Card} from '../../../api/models/Card';
import {CardMove} from '../../../api/models/card-move';
import {CardFormComponent} from '../../components/forms/card-form/card-form.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ToasterService} from 'angular5-toaster/dist';
import {Membership} from '../../../api/models/Membership';

@Component({
  selector: 'app-card-details',
  templateUrl: './card-details.component.html',
  styleUrls: ['./card-details.component.css']
})
export class CardDetailsComponent implements OnInit {

  id: string;
  card: Card;
  moves: CardMove[];
  editEnabled: boolean = false;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private api: ApiService,
              private apiService:ApiService,
              private modalService: NgbModal,
              protected toaster: ToasterService) { }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.api.card.get(this.id).subscribe(card => {
      this.card = card;
      this.moves = card.cardMoves.sort((a, b) => a.createdOn - b.createdOn);
      this.checkEdit();
      console.log(card);
    });
  }

  checkEdit() {
    this.api.project.get(this.card.project.id).subscribe(project => {
      if (project.membership != null) {
        let isDeveloper = Membership.isDeveloper(project.membership);
        let isProductOwner = Membership.isProductOwner(project.membership);
        let isKanbanMaster = Membership.isKanbanMaster(project.membership);
        let orderIndex = this.card.boardPart.orderIndex;

        this.api.board.get(project.board.id).subscribe(board => {
          let startDev = board.startDev;
          let accTesting = board.acceptanceTesting;

          if (orderIndex < startDev && (isKanbanMaster || isProductOwner) ||
            orderIndex >= startDev && orderIndex < accTesting && (isKanbanMaster || isDeveloper)) {
            this.editEnabled = true;
          }
        });
      }
    });
  }

  openCardEditModal() {
    const modalRef = this.modalService.open(CardFormComponent);
    (<CardFormComponent> modalRef.componentInstance).setInitialCard(this.card);

    modalRef.result
      .then(value =>
        this.apiService.card.put(value, true).subscribe(value => {
          this.toaster.pop("success", "Card was updated");
          this.router.navigate(['/card', value.id])
        }, error2 => {
          this.toaster.pop("error", "Error updating card");
        }));
  }

}
