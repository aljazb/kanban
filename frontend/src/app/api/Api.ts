import {HttpClient} from '@angular/common/http';
import {UserAccountResource} from './resource/UserAccountResource';
import {Injectable} from '@angular/core';
import {DevTeamResource} from './resource/DevTeamResource';
import {ProjectResource} from './resource/ProjectResource';
import {CardResource} from './resource/CardResource';
import {environment} from '../../environments/environment';
import {BoardResource} from './resource/BoardResource';

@Injectable()
export class ApiService {

  public url: string;

  public board: BoardResource;
  public card: CardResource;
  public devTeam: DevTeamResource;
  public project: ProjectResource;
  public userAccount: UserAccountResource;

  constructor(
    public httpClient: HttpClient) {

    this.httpClient = httpClient;
    this.url = environment.apiHostname + "/api/v1";

    this.initResources();
  }

  initResources() {
    this.board = new BoardResource(this);
    this.card = new CardResource(this);
    this.devTeam = new DevTeamResource(this);
    this.project = new ProjectResource(this);
    this.userAccount = new UserAccountResource(this);
  }

}
