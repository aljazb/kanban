import { Component, OnInit } from '@angular/core';
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


@Component({
  selector: 'app-analysis',
  templateUrl: './analysis.component.html',
  styleUrls: ['./analysis.component.css']
})
export class AnalysisComponent implements OnInit {

  query: AnalysisQuery = new AnalysisQuery();

  projectSelection: Project[];

  formQuery: FormGroup;

  fcProject: FormControl;

  fcCreatedFrom: FormControl;
  fcCreatedTo: FormControl;

  fcFinishedFrom: FormControl;
  fcFinishedTo: FormControl;

  fcDevStartFrom: FormControl;
  fcDevStartTo: FormControl;

  fcWorkloadFrom: FormControl;
  fcWorkloadTo: FormControl;

  fcIsSilverBullet: FormControl;
  fcIsRejected: FormControl;
  fcNewFunctionality: FormControl;

  leafBoardPartsSelection: BoardPartSelection[];

  workflowNgxDataSet: NgxDataSet;

  constructor(private api: ApiService) {
    api.analysis.getProjects().subscribe(value => this.projectSelection = value);

    this.initFormControls();
    this.initFormGroup();
  }

  initFormControls(): void {

    this.fcProject = new FormControl(null, Validators.required);
    this.fcProject.valueChanges.subscribe(value => {
      this.query.project = new Project();
      this.query.project.id = value.id;

      this.api.board.get(value.board.id).subscribe(board => {
        let bps = Board.getLeafParts(board.boardParts);
        bps.sort((a, b) => a.leafNumber - b.leafNumber);

        console.log(bps);

        this.leafBoardPartsSelection = [];
        bps.forEach(bp => {
          this.leafBoardPartsSelection.push(new BoardPartSelection(bp));
        });
      });
    });

    this.fcCreatedFrom = new FormControl();
    this.fcCreatedFrom.valueChanges.subscribe(value => {
      if(value == null) {
        delete this.query.createdFrom;
      } else{
        this.query.createdFrom = cDpToTs(value);
      }
    });

    this.fcCreatedTo = new FormControl();
    this.fcCreatedTo.valueChanges.subscribe(value => {
      if(value == null) {
        delete this.query.createdTo;
      } else{
        this.query.createdTo = cDpToTs(value);
      }
    });

    this.fcFinishedFrom = new FormControl();
    this.fcFinishedFrom.valueChanges.subscribe(value => {
      if(value == null) {
        delete this.query.finishedFrom;
      } else{
        this.query.finishedFrom = cDpToTs(value);
      }
    });

    this.fcFinishedTo = new FormControl();
    this.fcFinishedTo.valueChanges.subscribe(value => {
      if(value == null) {
        delete this.query.finishedTo;
      } else{
        this.query.finishedTo = cDpToTs(value);
      }
    });


    this.fcDevStartFrom = new FormControl();
    this.fcDevStartFrom.valueChanges.subscribe(value => {
      if(value == null) {
        delete this.query.devStartFrom;
      } else{
        this.query.devStartFrom = cDpToTs(value);
      }
    });

    this.fcDevStartTo = new FormControl();
    this.fcDevStartTo.valueChanges.subscribe(value => {
      if(value == null) {
        delete this.query.devStartTo;
      } else{
        this.query.devStartTo = cDpToTs(value);
      }
    });


    this.fcWorkloadFrom = new FormControl();
    this.fcWorkloadFrom.valueChanges.subscribe(value => {
      if(value == null) {
        delete this.query.workloadFrom;
      } else {
        this.query.workloadFrom = value;
      }
    });

    this.fcWorkloadTo = new FormControl();
    this.fcWorkloadTo.valueChanges.subscribe(value => {
      if(value == null) {
        delete this.query.workloadTo;
      } else {
        this.query.workloadTo = value;
      }
    });


    this.fcIsSilverBullet = new FormControl();
    this.fcIsSilverBullet.valueChanges.subscribe(value => {
      if(value) {
        this.resetType(this.fcIsSilverBullet);
        this.query.silverBullet = value;
      } else {
        delete this.query.silverBullet;
      }
    });

    this.fcIsRejected = new FormControl();
    this.fcIsRejected.valueChanges.subscribe(value => {
      if(value) {
        this.resetType(this.fcIsRejected);
        this.query.rejected = value;
      } else {
        delete this.query.rejected;
      }
    });

    this.fcNewFunctionality = new FormControl();
    this.fcNewFunctionality.valueChanges.subscribe(value => {
      if(value) {
        this.resetType(this.fcNewFunctionality);
        this.query.newFunctionality = value;
      } else {
        delete this.query.newFunctionality;
      }
    });
  }

  private resetType(source) {
    if(source != this.fcIsRejected) {
      this.fcIsRejected.patchValue(false);
    }

    if(source != this.fcIsSilverBullet) {
      this.fcIsSilverBullet.patchValue(false);
    }

    if(source != this.fcNewFunctionality) {
      this.fcNewFunctionality.patchValue(false);
    }
  }

  initFormGroup(): void {
    this.formQuery = new FormGroup({
      project: this.fcProject,
      createdFrom: this.fcCreatedFrom,
      createdTo: this.fcCreatedTo,
      finishedFrom: this.fcFinishedFrom,
      finishedTo: this.fcFinishedTo,
      devStartFrom: this.fcDevStartFrom,
      devStartTo: this.fcDevStartTo,
      workloadFrom: this.fcWorkloadFrom,
      workloadTo: this.fcWorkloadTo,
      isSilverBullet: this.fcIsSilverBullet,
      isRejected: this.fcIsRejected,
      newFunctionality: this.fcNewFunctionality
    });
  }

  ngOnInit() {

  }

  submitWorkFlow() {
    let query: WorkFlowQuery = Object.assign(new WorkFlowQuery(), this.query);

    this.leafBoardPartsSelection.forEach(bp =>  {
      if(bp.isActive) {

        let nbp = new BoardPart();
        nbp.id = bp.boardPart.id;

        query.leafBoardParts.push(nbp);
      }
    });

    this.workflowNgxDataSet = null;
    this.api.analysis.getWorkFlow(query).subscribe(value => {
      this.workflowNgxDataSet = value;
    });

  }

}
