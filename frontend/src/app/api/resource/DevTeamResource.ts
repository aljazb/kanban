import {CrudResource} from './base/CrudResource';
import {ApiService} from '../api.service';
import {DevTeam} from '../models/DevTeam';
import {Observable} from 'rxjs/Observable';
import {UserAccount} from '../models/UserAccount';
import {catchError} from 'rxjs/operators';

export class DevTeamResource extends CrudResource<DevTeam> {

  constructor(api: ApiService) {
    super("DevTeam", api);
  }

  getDevelopers(devTeamId: string): Observable<UserAccount[]> {
    return this.api.httpClient.get<UserAccount[]>(`${this.url}/${devTeamId}/developers`).pipe(
      catchError(this.handleError<UserAccount[]>("developers"))
    );
  }

  getKanbanMaster(devTeamId: string): Observable<UserAccount> {
    return this.api.httpClient.get<UserAccount>(`${this.url}/${devTeamId}/kanbanMaster`).pipe(
      catchError(this.handleError<UserAccount>("kanbanMaster"))
    );
  }

  getProductOwner(devTeamId: string): Observable<UserAccount> {
    return this.api.httpClient.get<UserAccount>(`${this.url}/${devTeamId}/productOwner`).pipe(
      catchError(this.handleError<UserAccount>("productOwner"))
    );
  }

  kickMember(devTeamId: string, memberId: string): Observable<UserAccount> {
    return this.api.httpClient.delete<UserAccount>(`${this.url}/${devTeamId}/user/${memberId}`).pipe(
      catchError(this.handleError<UserAccount>("kickMember"))
    );
  }

  demotePO(devTeamId: string, poId: string): Observable<UserAccount> {
    return this.api.httpClient.delete<UserAccount>(`${this.url}/${devTeamId}/po/${poId}`).pipe(
      catchError(this.handleError<UserAccount>("demotePO"))
    );
  }
}
