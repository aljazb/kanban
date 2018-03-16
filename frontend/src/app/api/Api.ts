import {HttpClient} from '@angular/common/http';
import {UserAccountResource} from './resource/UserAccountResource';
import {Injectable} from '@angular/core';
import {DevTeamResource} from './resource/DevTeamResource';
import {ProjectResource} from './resource/ProjectResource';
import {FlowTableResource} from './resource/FlowTableResource';
import {FlowTablePartResource} from './resource/FlowTablePartResource';
import {CardResource} from './resource/CardResource';
import {environment} from '../../environments/environment';

@Injectable()
export class ApiService {

  public url: string;

  public userAccount: UserAccountResource;
  public devTeam: DevTeamResource;
  public project: ProjectResource;
  public flowTable: FlowTableResource;
  public flowTablePart: FlowTablePartResource;
  public card: CardResource;


  constructor(public httpClient: HttpClient, url: string) {

    this.httpClient = httpClient;
    this.url = environment.apiHostname + "/api/v1";

    this.initResources();
  }

  initResources() {
    this.userAccount = new UserAccountResource(this);
    this.devTeam = new DevTeamResource(this);
    this.project = new ProjectResource(this);
    this.flowTable = new FlowTableResource(this);
    this.flowTablePart = new FlowTablePartResource(this);
    this.card = new CardResource(this);
  }

}
