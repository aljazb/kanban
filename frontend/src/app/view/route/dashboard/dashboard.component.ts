import {Component, OnInit} from '@angular/core';
import {LoginService} from '../../../api/services/login.service';
import {UserAccount} from '../../../api/models/UserAccount';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: [ './dashboard.component.css' ]
})
export class DashboardComponent implements OnInit {

  isLoggedIn: boolean = false;
  isKanbanMaster: boolean = false;
  user: UserAccount;
  constructor(
    public loginService: LoginService) { }


  login(): void {
    this.loginService.login();
  }

  ngOnInit() {
    this.loginService.getUser().subscribe(user => this.user = user);

    this.loginService.getUser().subscribe(user => {
      if(user != null) {
        this.isLoggedIn = true;
        this.isKanbanMaster = user.inRoleKanbanMaster;
      }
    });
  }

}
