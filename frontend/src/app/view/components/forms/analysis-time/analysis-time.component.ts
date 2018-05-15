import {Component, Input, OnInit} from '@angular/core';
import {AnalysisQuery} from '../../../../api/dto/analysis/analysis-query';
import {Project} from '../../../../api/models/Project';
import {ApiService} from '../../../../api/services/api.service';
import {Board} from '../../../../api/models/Board';
import {BoardPart} from '../../../../api/models/BoardPart';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {TimeQuery} from '../../../../api/dto/analysis/time/time-query';
import {SharedContext} from '../../../route/analysis/utility/shared-context';

@Component({
  selector: 'app-analysis-time',
  templateUrl: './analysis-time.component.html',
  styleUrls: ['./analysis-time.component.css']
})
export class AnalysisTimeComponent implements OnInit {

  @Input()
  sharedContext: SharedContext;

  leafBoardPartsSelection: BoardPart[];


  formTime: FormGroup;
  fcFrom: FormControl;
  fcTo: FormControl;

  leafBpFromSelection: BoardPart[];
  leafBpToSelection: BoardPart[];


  constructor(private api: ApiService) {
    this.initFormControl();
    this.initFormGroup();
  }


  ngOnInit() {
    this.handleProjectSelect(this.sharedContext.project);
  }


  private initFormControl(): void {
    this.fcFrom = new FormControl(null, Validators.required);
    this.fcFrom.valueChanges.subscribe(value => this.updateSelection());

    this.fcTo = new FormControl(null, Validators.required);
    this.fcTo.valueChanges.subscribe(value => this.updateSelection());
  }

  private initFormGroup(): void {
    this.formTime = new FormGroup({
      from: this.fcFrom,
      to: this.fcTo
    })
  }

  private updateSelection(): void {
    if(this.fcFrom.value != null) {
      let index = Number(this.fcFrom.value.leafNumber);
      this.leafBpToSelection = this.leafBoardPartsSelection.slice(index + 1);
    } else {
      this.leafBpToSelection = this.leafBoardPartsSelection;
    }

    if(this.fcTo.value != null) {
      let index = Number(this.fcTo.value.leafNumber);
      this.leafBpFromSelection = this.leafBoardPartsSelection.slice(0, index);
    } else {
      this.leafBpFromSelection = this.leafBoardPartsSelection;
    }
  }


  handleProjectSelect(project: Project) {

    this.fcFrom.patchValue(null);
    this.fcTo.patchValue(null);
    this.leafBoardPartsSelection = [];

    if(project != null && project.board) {
      this.api.board.get(project.board.id).subscribe(board => {
        let bps = Board.getLeafParts(board.boardParts);
        bps.sort((a, b) => a.leafNumber - b.leafNumber);
        this.leafBoardPartsSelection = bps;

        this.leafBpFromSelection = this.leafBoardPartsSelection;
        this.leafBpToSelection = this.leafBoardPartsSelection;
      });
    }
  }

  submitTime() {
    this.sharedContext.collapsed = true;
    let query: TimeQuery = Object.assign(new TimeQuery(), this.sharedContext.query);

    query.from = new BoardPart();
    query.from.id = this.fcFrom.value.id;

    query.to = new BoardPart();
    query.to.id = this.fcTo.value.id;

    this.api.analysis.getTime(query).subscribe(value => {
      console.log(value);
    });
  }

}
