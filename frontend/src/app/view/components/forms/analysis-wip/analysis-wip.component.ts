import {Component, Input, OnInit} from '@angular/core';
import {AnalysisQuery} from '../../../../api/dto/analysis/analysis-query';
import {ApiService} from '../../../../api/services/api.service';
import {WipQuery} from '../../../../api/dto/analysis/wip/wip-query';
import {Project} from '../../../../api/models/Project';

@Component({
  selector: 'app-analysis-wip',
  templateUrl: './analysis-wip.component.html',
  styleUrls: ['./analysis-wip.component.css']
})
export class AnalysisWipComponent implements OnInit {

  @Input()
  sharedContext: { collapsed: boolean, project: Project, query: AnalysisQuery };

  constructor(private api: ApiService) { }

  ngOnInit() {
  }

  handleProjectSelect(project: Project) {
    console.log(project);
  }

  submitWip() {
    this.sharedContext.collapsed = true;
    let query: WipQuery = Object.assign(new WipQuery(), this.sharedContext.query);
    this.api.analysis.getWip(query).subscribe(value => {
      console.log(value);
    });
  }

}
