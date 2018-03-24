import {Component, OnInit, ViewChild} from '@angular/core';
import {KeycloakAuthGuardService} from '../../../api/keycloak/keycloak-auth-guard.service';
import {UserAccount} from '../../../api/models/UserAccount';
import {ApiService} from '../../../api/Api';
import {UserPagingComponent} from '../../components/paging/user-paging/user-paging.component';


@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {

  @ViewChild(UserPagingComponent) userPagingComp;

  selectedUser: UserAccount;

  constructor(
    private keycloakGuard:KeycloakAuthGuardService,
    private api:ApiService) { }

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
}
