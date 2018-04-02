import { Component, OnInit } from '@angular/core';

import {LoginService} from '../../../api/login.service';
import {UserAccount} from '../../../api/models/UserAccount';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  navbarCollapsed = true;

  user: UserAccount;

  constructor(
    public loginService: LoginService) { }

  ngOnInit() {
    this.loginService.getUser().subscribe(user => {
      console.log(user);
      this.user = user;
    });
  }

  login(): void {
    this.loginService.login();
  }

  logout(): void {
    this.loginService.logout();
  }

}
