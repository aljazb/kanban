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
import {AnalysisQueryComponent} from '../../components/forms/analysis-query/analysis-query.component';
import {SharedContext} from './utility/shared-context';
import {UserAccount} from '../../../api/models/UserAccount';
import {LoginService} from '../../../api/services/login.service';


@Component({
  selector: 'app-analysis',
  templateUrl: './analysis.component.html',
  styleUrls: ['./analysis.component.css']
})
export class AnalysisComponent implements OnInit {

  @ViewChild(AnalysisQueryComponent)
  queryComponent: AnalysisQueryComponent;

  @ViewChild(AnalysisWorkflowComponent)
  workflowComponent: AnalysisWorkflowComponent;

  @ViewChild(AnalysisWipComponent)
  wipComponent: AnalysisWipComponent;

  @ViewChild(AnalysisTimeComponent)
  timeComponent: AnalysisTimeComponent;

  sharedContext: SharedContext = new SharedContext(1, null, new AnalysisQuery());

  authUser: UserAccount;

  constructor(private login: LoginService) {
  }

  ngOnInit() {
    this.login.getUser().subscribe(value => this.authUser = value);
  }

  handleOnProjectSelect(project: Project): void {
    if(this.workflowComponent != null) {
      this.workflowComponent.handleProjectSelect(project);
    } else if(this.wipComponent != null) {
      this.wipComponent.handleProjectSelect(project);
    } else if(this.timeComponent != null) {
      this.timeComponent.handleProjectSelect(project);
    }
  }

  handleTabChange(value: string) {
    this.sharedContext.activeTab = Number(value);
    if(this.queryComponent) {
      if(this.sharedContext.activeTab == 1 || this.sharedContext.activeTab == 2) {
        this.queryComponent.setShowFilterEnable(true);
      } else {
        this.queryComponent.setShowFilterEnable(false);
      }
    }
  }

}
