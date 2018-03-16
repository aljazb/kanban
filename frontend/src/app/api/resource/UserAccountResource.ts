import {UserAccount} from '../models/UserAccount';
import {CrudResource} from './base/CrudResource';
import {Observable} from 'rxjs/Observable';
import {ApiService} from '../Api';

export class UserAccountResource extends CrudResource<UserAccount> {

  constructor(api: ApiService) {
    super("UserAccount", api);
  }

  login (): Observable<UserAccount> {
    return this.api.httpClient.get<UserAccount>(this.url + "/login", { headers: this.getHeaders()});
  }

}
