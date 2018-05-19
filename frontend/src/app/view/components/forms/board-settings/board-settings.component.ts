import { Component } from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {Board} from '../../../../api/models/Board';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-board-settings',
  templateUrl: './board-settings.component.html',
  styleUrls: ['./board-settings.component.css']
})
export class BoardSettingsComponent {

  board: Board;

  formBoard: FormGroup;
  fcRemainingDays: FormControl;

  constructor(public activeModal: NgbActiveModal) {
    this.initFormControl();
    this.initFormGroup();
  }

  private initFormControl(): void {
    this.fcRemainingDays = new FormControl();
  }

  private initFormGroup(): void {
    this.formBoard = new FormGroup({
      remainingDays: this.fcRemainingDays
    });
  }

  setBoard(board: Board) {
    this.board = board;
    this.fcRemainingDays.patchValue(board.remainingDays);
  }

  onSubmit() {

    let b = new Board();
    b.id = this.board.id;
    b.remainingDays = this.fcRemainingDays.value;

    this.activeModal.close(b);
  }

}
