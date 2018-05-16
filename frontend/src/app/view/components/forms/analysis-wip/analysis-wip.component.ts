import {Component, Input, OnInit} from '@angular/core';
import {ApiService} from '../../../../api/services/api.service';
import {WipQuery} from '../../../../api/dto/analysis/wip/wip-query';
import {Project} from '../../../../api/models/Project';
import {SharedContext} from '../../../route/analysis/utility/shared-context';

@Component({
  selector: 'app-analysis-wip',
  templateUrl: './analysis-wip.component.html',
  styleUrls: ['./analysis-wip.component.css']
})
export class AnalysisWipComponent implements OnInit {

  @Input()
  sharedContext: SharedContext;

  constructor(private api: ApiService) { }

  ngOnInit() {

  }

  handleProjectSelect(project: Project) {
    console.log(project);
  }

  submitWip() {
    let query: WipQuery = Object.assign(new WipQuery(), this.sharedContext.query);
    this.api.analysis.getWip(query).subscribe(value => {
      console.log(value);
    });
  }

}
