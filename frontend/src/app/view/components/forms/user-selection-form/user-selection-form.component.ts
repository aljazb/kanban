import { Component, OnInit } from '@angular/core';
import {UserAccount} from '../../../../api/models/UserAccount';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {ApiService} from '../../../../api/services/api.service';
import {isNullOrUndefined} from 'util';

@Component({
  selector: 'app-user-selection-form',
  templateUrl: './user-selection-form.component.html',
  styleUrls: ['./user-selection-form.component.css']
})
export class UserSelectionFormComponent implements OnInit {

  selectedUser: string;
  users: UserAccount[];

  constructor(public activeModal: NgbActiveModal, private api: ApiService) {
  }

  ngOnInit() {
  }

  setUsers(users: UserAccount[]) {
    this.users = users;
  }

  selectUser() {
    if (isNullOrUndefined(this.selectedUser)) {
      this.activeModal.close();
    } else {
      this.activeModal.close(this.users.find(u => u.email == this.selectedUser));
    }
  }
}
