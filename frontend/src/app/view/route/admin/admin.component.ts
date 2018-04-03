import {Component, OnInit, ViewChild} from '@angular/core';
import {KeycloakAuthGuardService} from '../../../api/keycloak/keycloak-auth-guard.service';
import {UserAccount} from '../../../api/models/UserAccount';
import {ApiService} from '../../../api/api.service';
import {UserPagingComponent} from '../../components/paging/user-paging/user-paging.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {UserAccountFormComponent} from '../../components/forms/user-account-form/user-account-form.component';
import {ToasterService} from 'angular5-toaster/dist';


@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  @ViewChild(UserPagingComponent) userPagingComp;

  selectedUser: UserAccount;

  constructor(
    private keycloakGuard: KeycloakAuthGuardService,
    private api: ApiService,
    private toasterService: ToasterService,
    private modalService: NgbModal) { }

  ngOnInit() {
    this.keycloakGuard.handleAuth();
  }

  selectedUserCallback(user: UserAccount){
    this.selectedUser = user;
  }

  toggleDeleted(): void {
    this.api.userAccount.changeStatus(this.selectedUser.id, true).subscribe(value => {
        let actionStr = this.selectedUser.isDeleted ?
          "Activated user: " + this.selectedUser.username:
          "Deactivated user: " + this.selectedUser.username;

        this.toasterService.pop('success', actionStr);
        this.selectedUser = value;
        this.userPagingComp.refresh();
      }, error2 => {
        let actionStr = this.selectedUser.isDeleted ?
          'Error activating user: ' + this.selectedUser.username:
          'Error deactivating user: ' + this.selectedUser.username;

        this.toasterService.pop('error', actionStr);
        console.log(error2);
      });
  }

  openUserEditModal(user: UserAccount): void {
    const modalRef = this.modalService.open(UserAccountFormComponent);
    (<UserAccountFormComponent> modalRef.componentInstance).setInitialProject(user);
    modalRef.result.then(user => {
      this.api.userAccount.put(user, true).subscribe(value => {
        this.toasterService.pop('success', 'Updated user: ' + user.username);
        this.selectedUser = value;
        this.userPagingComp.refresh();
      }, error2 => {
        this.toasterService.pop('error', 'Error updating user: ' + user.username);
        console.log(error2);
      });
    }).catch(reason => console.log(reason));
  }

  openUserCreateModal(): void {
    const modalRef = this.modalService.open(UserAccountFormComponent);
    modalRef.result.then(user => {
      this.api.userAccount.post(user, true).subscribe(value => {
        this.toasterService.pop('success', 'Created new user: ' + user.username);
        this.selectedUser = value;
        this.userPagingComp.refresh();
      }, error2 => {
        this.toasterService.pop('error', 'Error creating user: ' + user.username);
        console.log(error2);
      });
    }).catch(reason => console.log(reason));
  }

}
