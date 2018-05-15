import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Project} from '../../../api/models/Project';
import {AnalysisQuery} from '../../../api/dto/analysis/analysis-query';
import {ApiService} from '../../../api/services/api.service';
import {Board} from '../../../api/models/Board';
import {BoardPartSelection} from './utility/board-part-selection';
import {WorkFlowQuery} from '../../../api/dto/analysis/work-flow-query';
import {BoardPart} from '../../../api/models/BoardPart';
import {cDpToTs} from '../../../utility';
import {NgxDataSet} from '../../../api/dto/ngx/grouped-series/ngx-data-set';
import {BoardBaseFormComponent} from '../../components/forms/board-form/board-base-form/board-base-form.component';
import {AnalysisWorkflowComponent} from '../../components/forms/analysis-workflow/analysis-workflow.component';


@Component({
  selector: 'app-analysis',
  templateUrl: './analysis.component.html',
  styleUrls: ['./analysis.component.css']
})
export class AnalysisComponent implements OnInit {

  @ViewChild(AnalysisWorkflowComponent)
  workflowComponent: AnalysisWorkflowComponent;

  query: AnalysisQuery = new AnalysisQuery();
  queryCollapsed: { value: boolean } = { value: false};

  constructor() {

  }

  ngOnInit() {  }

  handleOnProjectSelect(project: Project): void {
    this.workflowComponent.handleProjectSelect(project);
  }

  openQuery(): void {
    this.queryCollapsed.value = false;
  }

  closeQuery(): void {
    this.queryCollapsed.value = true;
  }

}
