import {CrudResource} from './base/CrudResource';
import {ApiService} from '../api.service';
import {DevTeam} from '../models/DevTeam';
import {Observable} from 'rxjs/Observable';
import {UserAccount} from '../models/UserAccount';
import {catchError, map} from 'rxjs/operators';

export class DevTeamResource extends CrudResource<DevTeam> {

  constructor(api: ApiService) {
    super("DevTeam", api);
  }

  getDevelopers(devTeamId: string): Observable<UserAccount[]> {
    return this.api.httpClient.get<UserAccount[]>(`${this.url}/${devTeamId}/developers`).pipe(
      catchError(this.handleError<UserAccount[]>("developers")),
      map(content => this.api.jsog.serialize(content))
    );
  }

  getKanbanMaster(devTeamId: string): Observable<UserAccount> {
    return this.api.httpClient.get<UserAccount>(`${this.url}/${devTeamId}/kanbanMaster`).pipe(
      catchError(this.handleError<UserAccount>("kanbanMaster")),
      map(content => this.api.jsog.serialize(content))
    );
  }

  getProductOwner(devTeamId: string): Observable<UserAccount> {
    return this.api.httpClient.get<UserAccount>(`${this.url}/${devTeamId}/productOwner`).pipe(
      catchError(this.handleError<UserAccount>("productOwner")),
      map(content => this.api.jsog.serialize(content))
    );
  }
}
