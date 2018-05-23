import {BaseResource} from './base/BaseResource';
import {AnalysisResponse} from '../dto/analysis/analysis-response';
import {ApiService} from '../services/api.service';
import {map} from 'rxjs/operators';
import {Observable} from 'rxjs/Observable';
import {WorkflowQuery} from '../dto/analysis/workflow/workflow-query';
import {Project} from '../models/Project';
import {NgxDataSet} from '../dto/ngx/grouped-series/ngx-data-set';
import {WipQuery} from '../dto/analysis/wip/wip-query';
import {WipResponse} from '../dto/analysis/wip/wip-response';
import {TimeQuery} from '../dto/analysis/time/time-query';
import {TimeResponse} from '../dto/analysis/time/time-response';
import {DeveloperRatioQuery} from '../dto/analysis/devRatio/developer-ratio-query';
import {DeveloperRatioResponse} from '../dto/analysis/devRatio/developer-ratio-response';

export class AnalysisResource extends BaseResource<AnalysisResponse> {

  constructor(api: ApiService) {
    super("Analysis", api);
  }

  getProjects (): Observable<Project[]> {
    return this.api.httpClient.get<Project[]>(this.url + "/Project", { headers: this.getHeaders(false)})
      .pipe(map(content => this.deserialize(content)));
  }

  getWorkFlow (entity: WorkflowQuery): Observable<NgxDataSet[]> {
    return this.api.httpClient.put<NgxDataSet[]>(this.url + "/WorkFlow", this.serialize(entity), { headers: this.getHeaders(false)})
      .pipe(map(content => this.deserialize(content)));
  }

  getWip (entity: WipQuery): Observable<WipResponse> {
    return this.api.httpClient.put<WipResponse>(this.url + "/Wip", this.serialize(entity), { headers: this.getHeaders(false)})
      .pipe(map(content => this.deserialize(content)));
  }

  getTime (entity: TimeQuery): Observable<TimeResponse> {
    return this.api.httpClient.put<TimeResponse>(this.url + "/Time", this.serialize(entity), { headers: this.getHeaders(false)})
      .pipe(map(content => this.deserialize(content)));
  }

  getDevRatio (entity: DeveloperRatioQuery): Observable<DeveloperRatioResponse> {
    return this.api.httpClient.put<DeveloperRatioResponse>(this.url + "/DevRatio", this.serialize(entity), { headers: this.getHeaders(false)})
      .pipe(map(content => this.deserialize(content)));
  }
}
