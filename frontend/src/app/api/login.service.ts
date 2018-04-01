import { Injectable } from '@angular/core';
import {ApiService} from './api.service';
import {UserAccount} from './models/UserAccount';
import {Observable} from 'rxjs';
import {KeycloakAuthGuardService} from './keycloak/keycloak-auth-guard.service';
import {of} from 'rxjs/observable/of';
import {tap} from 'rxjs/operators';

@Injectable()
export class LoginService {

  private _userRequest: Observable<UserAccount> = null;
  private _isLoggedIn: boolean = false;
  private _user: UserAccount = null;

  constructor(
    public keycloak: KeycloakAuthGuardService,
    private api: ApiService) {
  }

  private getUserRequest(isLoggedIn: boolean): Observable<UserAccount> {
    this._isLoggedIn = isLoggedIn;
    if(this._isLoggedIn) {
      return this.api.userAccount.login().pipe(tap(user => this._user = user));
    } else {
      return of(null);
    }
  }

  getUser(): Observable<UserAccount> {
    if(this._user){
      return of(this._user);
    } else if(this._userRequest){
      return this._userRequest;
    } else {
      this._userRequest = Observable.fromPromise(this.keycloak.isLoggedIn())
        .switchMap(isLoggedIn => this.getUserRequest(isLoggedIn)).share();
      return this._userRequest;
    }
  }

  isLoggedIn(): Observable<boolean> {
    if(this._isLoggedIn){
      return of(this._isLoggedIn);
    } else if(this._userRequest){
      return this._userRequest.map(user => user != null);
    } else {
      this._userRequest = Observable.fromPromise(this.keycloak.isLoggedIn())
        .switchMap(isLoggedIn => this.getUserRequest(isLoggedIn)).share();

      return this._userRequest.map(u => u == null);
    }
  }

  login(): void {
    this.keycloak.login();
  }

  logout(): void {
    this.keycloak.logout();
  }


}
