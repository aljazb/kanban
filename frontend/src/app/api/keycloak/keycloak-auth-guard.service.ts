import { Injectable } from '@angular/core';
import {CanActivate, Router, ActivatedRoute, ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router';
import { KeycloakService, KeycloakAuthGuard } from 'keycloak-angular';

@Injectable()
export class KeycloakAuthGuardService extends KeycloakAuthGuard {
  constructor(
    protected router: Router,
    protected keycloak: KeycloakService) {
    super(router, keycloak);
  }

  isAccessAllowed(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean> {
    return new Promise(async (resolve, reject) => {
      if (!this.authenticated) {
        this.keycloak.login();
        return;
      }

      const requiredRoles = route.data.roles;
      if (!requiredRoles || requiredRoles.length === 0) {
        return resolve(true);
      } else {
        if (!this.roles || this.roles.length === 0) {
          resolve(false);
        }
        let granted: boolean = false;
        for (const requiredRole of requiredRoles) {
          if (this.roles.indexOf(requiredRole) > -1) {
            granted = true;
            break;
          }
        }
        resolve(granted);
      }
    });
  }

  handleAuth(): void {
    let state = this.router.routerState.snapshot;
    let active = this.router.routerState.snapshot.root;
    this.canActivate(active, state).then(value => {
      if(!value){
        console.log("KeycloakGuard: Access denied.");
        this.router.navigate(['dashboard']);
      }
    });
  }

  login(){
    this.keycloak.login();
  }

  logout(): void {
    this.router.navigate(['dashboard'])
      .then(value => this.keycloak.logout());
  }

  isLoggedIn(): Promise<boolean> {
    return this.keycloak.isLoggedIn();
  }

  isUserInRole(role: string): boolean {
    return this.keycloak.isUserInRole(role);
  }
}
