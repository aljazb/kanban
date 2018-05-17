import { Component, OnInit } from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-card-delete-confirmation',
  templateUrl: './card-delete-confirmation.component.html',
  styleUrls: ['./card-delete-confirmation.component.css']
})
export class CardDeleteConfirmationComponent {

  constructor(public activeModal: NgbActiveModal) { }

  deleteCard() {
    this.activeModal.close();
  }

  cancelCard() {
    this.activeModal.dismiss();
  }
}
