import { Component, OnInit } from '@angular/core';
import {UserAccount} from '../../../../api/models/UserAccount';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {isNullOrUndefined} from 'util';
import {ApiService} from '../../../../api/api.service';

@Component({
  selector: 'app-user-selection-form',
  templateUrl: './user-selection-form.component.html',
  styleUrls: ['./user-selection-form.component.css']
})
export class UserSelectionFormComponent implements OnInit {

  selectedUser: string;
  users: UserAccount[];

  private predicate;

  constructor(public activeModal: NgbActiveModal, private api: ApiService) {
  }

  loadUsers() {
    this.api.userAccount.getList().subscribe(uaPg => {
      this.users = uaPg.items;
      this.selectedUser = this.users[0].email;
      this.users = this.users.filter(this.predicate);
    });
  }

  ngOnInit() {
    this.loadUsers();
  }

  filterUsersWith(predicate) {
    this.predicate = predicate;
    if (!isNullOrUndefined(this.users)) {
      this.users = this.users.filter(predicate);
    }
  }

  selectUser() {
    this.activeModal.close(this.users.find(u => u.email == this.selectedUser));
  }
}
