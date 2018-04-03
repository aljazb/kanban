import { Component, OnInit } from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-project-delete-confirmation',
  templateUrl: './project-delete-confirmation.component.html',
  styleUrls: ['./project-delete-confirmation.component.css']
})
export class ProjectDeleteConfirmationComponent {

  constructor(public activeModal: NgbActiveModal) { }

  ngOnInit() {
  }

  deleteProject() {
    this.activeModal.close();
  }

  cancel() {
    this.activeModal.dismiss();
  }

}
