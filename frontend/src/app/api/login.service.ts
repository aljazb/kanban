import { Injectable } from '@angular/core';
import {ApiService} from './Api';
import {UserAccount} from './models/UserAccount';
import 'rxjs/add/observable/of';

@Injectable()
export class LoginService {

  user: UserAccount;

  constructor(private api: ApiService) {
  }

  loginApi(): void {
    this.api.userAccount.login().subscribe((user) => {
      this.user = user;
    });
  }
}
