import { Component } from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-card-move-confirmation',
  templateUrl: './card-move-confirmation.component.html',
  styleUrls: ['./card-move-confirmation.component.css']
})
export class CardMoveConfirmationComponent {

  WIPViolationReason: string = '';

  constructor(public activeModal: NgbActiveModal) { }

  yes(): void {
    this.activeModal.close(this.WIPViolationReason);
  }

  no(): void {
    this.activeModal.dismiss();
  }

}
