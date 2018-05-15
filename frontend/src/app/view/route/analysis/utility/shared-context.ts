import {AnalysisQuery} from '../../../../api/dto/analysis/analysis-query';
import {Project} from '../../../../api/models/Project';

export class SharedContext {
  collapsed: boolean;
  activeTab: number;
  project: Project;
  query: AnalysisQuery;


  constructor(collapsed: boolean, activeTab: number, project: Project, query: AnalysisQuery) {
    this.collapsed = collapsed;
    this.activeTab = activeTab;
    this.project = project;
    this.query = query;
  }
}
