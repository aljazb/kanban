import {AfterViewChecked, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {BoardPart} from '../../../../api/models/BoardPart';
import {NgxDataSet} from '../../../../api/dto/ngx/grouped-series/ngx-data-set';
import {WorkflowQuery} from '../../../../api/dto/analysis/workflow/workflow-query';
import {BoardPartSelection} from '../../../route/analysis/utility/board-part-selection';
import {AnalysisQuery} from '../../../../api/dto/analysis/analysis-query';
import {ApiService} from '../../../../api/services/api.service';
import {Board} from '../../../../api/models/Board';
import {Project} from '../../../../api/models/Project';
import {SharedContext} from '../../../route/analysis/utility/shared-context';

@Component({
  selector: 'app-analysis-workflow',
  templateUrl: './analysis-workflow.component.html',
  styleUrls: ['./analysis-workflow.component.css']
})
export class AnalysisWorkflowComponent implements OnInit {


  @ViewChild('workflowTab')
  workflowTabComponent: ElementRef;

  @Input()
  sharedContext: SharedContext;

  leafBoardPartsSelection: BoardPartSelection[];

  workflowNgxDataSet: NgxDataSet[];

  workflowNgxDataSetDisplay: NgxDataSet[];
  wfScrollValue: number;
  wfMaxDisplay: number = 4;
  showWfGraph: boolean = false;

  constructor(private api: ApiService) { }

  ngOnInit() {
    this.handleProjectSelect(this.sharedContext.project);
  }

  handleProjectSelect(project: Project) {
    this.leafBoardPartsSelection = [];

    if(project != null && project.board) {
      this.api.board.get(project.board.id).subscribe(board => {
        let bps = Board.getLeafParts(board.boardParts);
        bps.sort((a, b) => a.leafNumber - b.leafNumber);
        bps.forEach(bp => {
          this.leafBoardPartsSelection.push(new BoardPartSelection(bp));
        });
      });
    }
  }

  submitWorkFlow() {
    let query: WorkflowQuery = Object.assign(new WorkflowQuery(), this.sharedContext.query);

    this.leafBoardPartsSelection.forEach(bp =>  {
      if(bp.isActive) {

        let nbp = new BoardPart();
        nbp.id = bp.boardPart.id;

        query.leafBoardParts.push(nbp);
      }
    });

    this.workflowNgxDataSet = null;
    this.api.analysis.getWorkFlow(query).subscribe(value => {
      this.setWorkflowNgxDataSet(value);
    });
  }

  private setWorkflowNgxDataSet(dataSet: NgxDataSet[]) {
    this.workflowNgxDataSetDisplay = null;
    this.workflowNgxDataSet = dataSet;

    this.showWfGraph = false;
    this.updateDisplay(0);

    setTimeout(() => {
      this.showWfGraph = true;
      setTimeout(() => { this.scrollToBottom(); }, 400);
    }, 100);
  }

  scrollToBottom(): void {
    window.scrollTo({ left: 0, top: document.body.scrollHeight, behavior: 'smooth' });
  }

  updateDisplay(value: number, max:number=null): void {
    if(max) this.wfMaxDisplay = Number(max);
    this.wfScrollValue = value;
    this.workflowNgxDataSetDisplay = this.workflowNgxDataSet.slice(this.wfScrollValue, this.wfScrollValue + this.wfMaxDisplay);
  }

  getWorkflowMaxValue(){
    if(this.workflowNgxDataSet) {
      return this.workflowNgxDataSet.length - this.wfMaxDisplay;
    } else {
      return 0;
    }
  }


  workflowScrollChanged(value: number) {
    this.updateDisplay(Number(value));
  }

}
