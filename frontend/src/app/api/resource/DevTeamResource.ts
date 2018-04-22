import {CrudResource} from './base/CrudResource';
import {ApiService} from '../api.service';
import {DevTeam} from '../models/DevTeam';
import {Observable} from 'rxjs/Observable';
import {UserAccount} from '../models/UserAccount';
import {catchError, map} from 'rxjs/operators';
import {HistoryEvent} from '../models/HistoryEvent';

export class DevTeamResource extends CrudResource<DevTeam> {

  constructor(api: ApiService) {
    super("DevTeam", api);
  }

  kickMember(devTeamId: string, memberId: string): Observable<UserAccount> {
    return this.api.httpClient.delete<UserAccount>(`${this.url}/${devTeamId}/user/${memberId}`)
      .pipe(map(content => this.deserialize(content)));
  }

  demotePO(devTeamId: string, poId: string): Observable<UserAccount> {
    return this.api.httpClient.delete<UserAccount>(`${this.url}/${devTeamId}/po/${poId}`)
      .pipe(map(content => this.deserialize(content)));
  }

  getEvents(devTeamId: string): Observable<HistoryEvent[]> {
    return this.api.httpClient.get<HistoryEvent[]>(`${this.url}/${devTeamId}/events`)
      .pipe(map(content => this.deserializeArray(content)));
  }
}
