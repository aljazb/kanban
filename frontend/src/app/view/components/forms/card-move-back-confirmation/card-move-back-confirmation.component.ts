import { Component, OnInit } from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {BoardPart} from '../../../../api/models/BoardPart';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-card-move-back-confirmation',
  templateUrl: './card-move-back-confirmation.component.html',
  styleUrls: ['./card-move-back-confirmation.component.css']
})
export class CardMoveBackConfirmationComponent {

  form: FormGroup;
  fcMoveTo: FormControl;

  boardPartsSelection: BoardPart[] = [];

  constructor(public activeModal: NgbActiveModal) {
    this.initFormControl();
    this.initFormGroup();
  }

  private initFormControl(): void {
    this.fcMoveTo = new FormControl(null, Validators.required);
  }

  private initFormGroup(): void {
    this.form = new FormGroup({
      moveTo: this.fcMoveTo
    })
  }

  setBoardParts(boardPart: BoardPart[]): void {
    this.boardPartsSelection = boardPart;
  }

  yes(): void {
    this.activeModal.close(this.fcMoveTo.value);
  }

  no(): void {
    this.activeModal.dismiss();
  }

}
