import {Component, Input, OnInit} from '@angular/core';
import {AnalysisQuery} from '../../../../api/dto/analysis/analysis-query';
import {Project} from '../../../../api/models/Project';
import {ApiService} from '../../../../api/services/api.service';
import {Board} from '../../../../api/models/Board';
import {BoardPart} from '../../../../api/models/BoardPart';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {TimeQuery} from '../../../../api/dto/analysis/time/time-query';
import {SharedContext} from '../../../route/analysis/utility/shared-context';
import {TimeResponse} from '../../../../api/dto/analysis/time/time-response';
import {isNullOrUndefined} from 'util';

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

  response: TimeResponse;

  formFormat: FormGroup;
  fcFormat: FormControl;
  timeUnit: string;

  cardToTime: Map<string, string>;
  averageTime: string;


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

    this.timeUnit = "h";
    this.fcFormat = new FormControl("h", Validators.required);
    this.fcFormat.valueChanges.subscribe(value => {
      this.timeUnit = value;
      this.updateCardTimes();
    });
  }

  private initFormGroup(): void {
    this.formTime = new FormGroup({
      from: this.fcFrom,
      to: this.fcTo
    });
    this.formFormat = new FormGroup({
      format: this.fcFormat
    });
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
      this.response = value;
      this.cardToTime = new Map<string, string>();
      this.averageTime = undefined;
      this.updateCardTimes();
    });
  }

  private convertTime(time: number) : string {
    let timeConverted: number = null;
    switch (this.timeUnit) {
      case "s":
        timeConverted = time;
        break;
      case "min":
        timeConverted = time / 60.0;
        break;
      case "h":
        timeConverted = time / (60.0 * 60);
        break;
      case "d":
        timeConverted = time / (60.0 * 60 * 24);
        break;
    }

    return Number(timeConverted.toPrecision(2)).toString();
  }

  private updateCardTimes() {
    if (!isNullOrUndefined(this.response)) {
      this.response.cards.forEach(tc => {
        this.cardToTime.set(tc.card.id, this.convertTime(tc.time));
      });
      this.averageTime = this.convertTime(this.response.averageTime);
    }
  }
}
