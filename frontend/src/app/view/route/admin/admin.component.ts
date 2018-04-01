import {Component, OnInit, ViewChild} from '@angular/core';
import {KeycloakAuthGuardService} from '../../../api/keycloak/keycloak-auth-guard.service';
import {UserAccount} from '../../../api/models/UserAccount';
import {ApiService} from '../../../api/api.service';
import {UserPagingComponent} from '../../components/paging/user-paging/user-paging.component';
import {ProjectFormComponent} from '../../components/forms/project-form/project-form.component';
import {Project} from '../../../api/models/Project';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {UserAccountFormComponent} from '../../components/forms/user-account-form/user-account-form.component';


@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  @ViewChild(UserPagingComponent) userPagingComp;

  selectedUser: UserAccount;

  constructor(
    private keycloakGuard:KeycloakAuthGuardService,
    private api:ApiService,
    private modalService: NgbModal) { }

  ngOnInit() {
    this.keycloakGuard.handleAuth();
  }

  selectedUserCallback(user: UserAccount){
    this.selectedUser = user;
  }

  toggleDeleted(): void {
    this.api.userAccount.changeStatus(this.selectedUser.id, true)
      .subscribe(value => {
        this.selectedUser = value;
        this.userPagingComp.refresh();
      });
  }

  openUserEditModal(user: UserAccount): void {
    const modalRef = this.modalService.open(UserAccountFormComponent);
    (<UserAccountFormComponent> modalRef.componentInstance).setInitialProject(user);
    modalRef.result.then(user => {
      this.api.userAccount.put(user, true).subscribe(value => {
        console.log("Response");
        console.log(value);
      });
    }).catch(reason => console.log(reason));
  }

  openUserCreateModal(): void {
    const modalRef = this.modalService.open(UserAccountFormComponent);
    modalRef.result.then(user => {
      this.api.userAccount.post(user, true).subscribe(value => {
        console.log("Response");
        console.log(value);
      });
    }).catch(reason => console.log(reason));
  }

}
