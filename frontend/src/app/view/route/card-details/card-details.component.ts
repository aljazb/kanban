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

      if (card.cardMoves) {
        this.moves = card.cardMoves.sort((a, b) => a.createdOn - b.createdOn);
      }

      this.checkEdit();
    });
  }

  checkEdit() {
    let b: Board = this.card.boardPart.board;
    let m: Membership = this.card.membership;

    if (m != null) {
      let leafIndex = this.card.boardPart.leafNumber;

      if(Membership.isKanbanMaster(m)) {
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
        }
      }
    }
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
