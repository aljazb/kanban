import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Project} from '../../../../api/models/Project';
import {AnalysisQuery} from '../../../../api/dto/analysis/analysis-query';
import {cDpToTs} from '../../../../utility';
import {ApiService} from '../../../../api/services/api.service';

@Component({
  selector: 'app-analysis-query',
  templateUrl: './analysis-query.component.html',
  styleUrls: ['./analysis-query.component.css']
})
export class AnalysisQueryComponent implements OnInit {

  @Output()
  onProjectSelected: EventEmitter<Project> = new EventEmitter();

  @Input()
  sharedContext: { collapsed: boolean, project: Project, query: AnalysisQuery };


  projectSelection: Project[];

  formQuery: FormGroup;

  fcProject: FormControl;

  fcShowFrom: FormControl;
  fcShowTo: FormControl;

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

  constructor(private api: ApiService) {
    api.analysis.getProjects().subscribe(value => this.projectSelection = value);

    this.initFormControls();
    this.initFormGroup();
  }

  ngOnInit() {
  }

  initFormControls(): void {

    this.fcProject = new FormControl(null, Validators.required);
    this.fcProject.valueChanges.subscribe(value => {
      this.sharedContext.project = value;
      if(value == null) {
        this.sharedContext.query.project = null;
      } else {
        this.sharedContext.query.project = new Project();
        this.sharedContext.query.project.id = value.id;
      }

      this.onProjectSelected.emit(value);
    });

    this.fcShowFrom = new FormControl();
    this.fcShowFrom.valueChanges.subscribe(value => {
      if(value == null) {
        delete this.sharedContext.query.showFrom;
      } else{
        this.sharedContext.query.showFrom = cDpToTs(value);
      }
    });

    this.fcShowTo = new FormControl();
    this.fcShowTo.valueChanges.subscribe(value => {
      if(value == null) {
        delete this.sharedContext.query.showTo;
      } else{
        this.sharedContext.query.showTo = cDpToTs(value);
      }
    });

    this.fcCreatedFrom = new FormControl();
    this.fcCreatedFrom.valueChanges.subscribe(value => {
      if(value == null) {
        delete this.sharedContext.query.createdFrom;
      } else{
        this.sharedContext.query.createdFrom = cDpToTs(value);
      }
    });

    this.fcCreatedTo = new FormControl();
    this.fcCreatedTo.valueChanges.subscribe(value => {
      if(value == null) {
        delete this.sharedContext.query.createdTo;
      } else{
        this.sharedContext.query.createdTo = cDpToTs(value);
      }
    });

    this.fcFinishedFrom = new FormControl();
    this.fcFinishedFrom.valueChanges.subscribe(value => {
      if(value == null) {
        delete this.sharedContext.query.finishedFrom;
      } else{
        this.sharedContext.query.finishedFrom = cDpToTs(value);
      }
    });

    this.fcFinishedTo = new FormControl();
    this.fcFinishedTo.valueChanges.subscribe(value => {
      if(value == null) {
        delete this.sharedContext.query.finishedTo;
      } else{
        this.sharedContext.query.finishedTo = cDpToTs(value);
      }
    });


    this.fcDevStartFrom = new FormControl();
    this.fcDevStartFrom.valueChanges.subscribe(value => {
      if(value == null) {
        delete this.sharedContext.query.devStartFrom;
      } else{
        this.sharedContext.query.devStartFrom = cDpToTs(value);
      }
    });

    this.fcDevStartTo = new FormControl();
    this.fcDevStartTo.valueChanges.subscribe(value => {
      if(value == null) {
        delete this.sharedContext.query.devStartTo;
      } else{
        this.sharedContext.query.devStartTo = cDpToTs(value);
      }
    });


    this.fcWorkloadFrom = new FormControl();
    this.fcWorkloadFrom.valueChanges.subscribe(value => {
      if(value == null) {
        delete this.sharedContext.query.workloadFrom;
      } else {
        this.sharedContext.query.workloadFrom = value;
      }
    });

    this.fcWorkloadTo = new FormControl();
    this.fcWorkloadTo.valueChanges.subscribe(value => {
      if(value == null) {
        delete this.sharedContext.query.workloadTo;
      } else {
        this.sharedContext.query.workloadTo = value;
      }
    });


    this.fcIsSilverBullet = new FormControl();
    this.fcIsSilverBullet.valueChanges.subscribe(value => {
      if(value) {
        this.resetType(this.fcIsSilverBullet);
        this.sharedContext.query.silverBullet = value;
      } else {
        delete this.sharedContext.query.silverBullet;
      }
    });

    this.fcIsRejected = new FormControl();
    this.fcIsRejected.valueChanges.subscribe(value => {
      if(value) {
        this.resetType(this.fcIsRejected);
        this.sharedContext.query.rejected = value;
      } else {
        delete this.sharedContext.query.rejected;
      }
    });

    this.fcNewFunctionality = new FormControl();
    this.fcNewFunctionality.valueChanges.subscribe(value => {
      if(value) {
        this.resetType(this.fcNewFunctionality);
        this.sharedContext.query.newFunctionality = value;
      } else {
        delete this.sharedContext.query.newFunctionality;
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
      showFrom: this.fcShowFrom,
      showTo: this.fcShowTo,
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


}
