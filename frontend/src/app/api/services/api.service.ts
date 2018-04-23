import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {JsogService} from 'jsog-typescript';
import {BoardResource} from '../resource/BoardResource';
import {CardResource} from '../resource/CardResource';
import {DevTeamResource} from '../resource/DevTeamResource';
import {ProjectResource} from '../resource/ProjectResource';
import {UserAccountResource} from '../resource/UserAccountResource';
import {RequestResource} from '../resource/RequestResource';
import {CardMoveResource} from '../resource/CardMoveResource';

@Injectable()
export class ApiService {

  public url: string;
  private _xContent: boolean;
  public jsog = new JsogService();

  public board: BoardResource;
  public card: CardResource;
  public devTeam: DevTeamResource;
  public project: ProjectResource;
  public userAccount: UserAccountResource;
  public request: RequestResource;
  public cardMove: CardMoveResource;

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
    this.request = new RequestResource(this);
    this.cardMove = new CardMoveResource(this);
  }


  get xContent(): boolean {
    return this._xContent;
  }

  set xContent(value: boolean) {
    this._xContent = value;
  }
}
