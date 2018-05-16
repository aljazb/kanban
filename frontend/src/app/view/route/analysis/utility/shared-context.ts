import {AnalysisQuery} from '../../../../api/dto/analysis/analysis-query';
import {Project} from '../../../../api/models/Project';

export class SharedContext {
  activeTab: number;
  project: Project;
  query: AnalysisQuery;


  constructor(activeTab: number, project: Project, query: AnalysisQuery) {
    this.activeTab = activeTab;
    this.project = project;
    this.query = query;
  }
}
