import {Component, OnInit} from '@angular/core';
import {LoginService} from '../../../api/login.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: [ './dashboard.component.css' ]
})
export class DashboardComponent implements OnInit {

  isLoggedIn: boolean = false;
  isKanbanMaster: boolean = false;

  constructor(
    public loginService: LoginService) { }


  login(): void {
    this.loginService.login();
  }

  ngOnInit() {
    this.loginService.getUser().subscribe(user => {
      if(user != null) {
        this.isLoggedIn = true;
        this.isKanbanMaster = user.inRoleKanbanMaster;
      }
    });
  }

}
