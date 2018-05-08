import { Component, OnInit } from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-column-wip-violation-confirmation',
  templateUrl: './column-wip-violation-confirmation.component.html',
  styleUrls: ['./column-wip-violation-confirmation.component.css']
})
export class ColumnWipViolationConfirmationComponent implements OnInit {

  exceedingColumns: string[];

  constructor(public activeModal: NgbActiveModal) { }

  ngOnInit() {
  }

  setExceedingColumns(columns: string[]) {
    this.exceedingColumns = columns;
  }

  yes(): void {
    this.activeModal.close();
  }

  no(): void {
    this.activeModal.dismiss();
  }

}
