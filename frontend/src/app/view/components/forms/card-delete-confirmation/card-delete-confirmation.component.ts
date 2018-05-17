import { Component, OnInit } from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-card-delete-confirmation',
  templateUrl: './card-delete-confirmation.component.html',
  styleUrls: ['./card-delete-confirmation.component.css']
})
export class CardDeleteConfirmationComponent {

  deleteMessage: string = '';

  constructor(public activeModal: NgbActiveModal) { }


  deleteCard() {
    this.activeModal.close(this.deleteMessage);
  }

  cancelCard() {
    this.activeModal.dismiss();
  }
}
