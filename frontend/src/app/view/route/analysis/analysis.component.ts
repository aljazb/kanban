import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Project} from '../../../api/models/Project';
import {AnalysisQuery} from '../../../api/dto/analysis/analysis-query';
import {ApiService} from '../../../api/services/api.service';
import {Board} from '../../../api/models/Board';
import {BoardPartSelection} from './utility/board-part-selection';
import {WorkflowQuery} from '../../../api/dto/analysis/workflow/workflow-query';
import {BoardPart} from '../../../api/models/BoardPart';
import {cDpToTs} from '../../../utility';
import {NgxDataSet} from '../../../api/dto/ngx/grouped-series/ngx-data-set';
import {BoardBaseFormComponent} from '../../components/forms/board-form/board-base-form/board-base-form.component';
import {AnalysisWorkflowComponent} from '../../components/forms/analysis-workflow/analysis-workflow.component';
import {AnalysisWipComponent} from '../../components/forms/analysis-wip/analysis-wip.component';
import {AnalysisTimeComponent} from '../../components/forms/analysis-time/analysis-time.component';


@Component({
  selector: 'app-analysis',
  templateUrl: './analysis.component.html',
  styleUrls: ['./analysis.component.css']
})
export class AnalysisComponent implements OnInit {

  @ViewChild(AnalysisWorkflowComponent)
  workflowComponent: AnalysisWorkflowComponent;

  @ViewChild(AnalysisWipComponent)
  wipComponent: AnalysisWipComponent;

  @ViewChild(AnalysisTimeComponent)
  timeComponent: AnalysisTimeComponent;

  sharedContext: { collapsed: boolean, project: Project, query: AnalysisQuery } =
    { collapsed: false, project: null, query: new AnalysisQuery() };

  constructor() {

  }

  ngOnInit() {  }

  handleOnProjectSelect(project: Project): void {
    if(this.workflowComponent != null) {
      this.workflowComponent.handleProjectSelect(project);
    } else if(this.wipComponent != null) {
      this.wipComponent.handleProjectSelect(project);
    } else if(this.timeComponent != null) {
      this.timeComponent.handleProjectSelect(project);
    }
  }

  openQuery(): void {
    this.sharedContext.collapsed = false;
  }

  closeQuery(): void {
    this.sharedContext.collapsed = true;
  }

}
