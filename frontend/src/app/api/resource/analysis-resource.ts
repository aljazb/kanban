import {BaseResource} from './base/BaseResource';
import {AnalysisResponse} from '../dto/analysis/analysis-response';
import {ApiService} from '../services/api.service';
import {map} from 'rxjs/operators';
import {Observable} from 'rxjs/Observable';
import {WorkFlowQuery} from '../dto/analysis/work-flow-query';
import {Project} from '../models/Project';
import {NgxDataSet} from '../dto/ngx/grouped-series/ngx-data-set';

export class AnalysisResource extends BaseResource<AnalysisResponse> {

  constructor(api: ApiService) {
    super("Analysis", api);
  }

  getProjects (): Observable<Project[]> {
    return this.api.httpClient.get<Project[]>(this.url + "/Project", { headers: this.getHeaders(false)})
      .pipe(map(content => this.deserialize(content)));
  }

  getWorkFlow (entity: WorkFlowQuery): Observable<NgxDataSet> {
    return this.api.httpClient.put<NgxDataSet>(this.url + "/WorkFlow", this.serialize(entity), { headers: this.getHeaders(false)})
      .pipe(map(content => this.deserialize(content)));
  }

}
