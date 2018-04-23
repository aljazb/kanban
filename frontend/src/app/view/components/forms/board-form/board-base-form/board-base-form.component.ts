import {Component, Input, OnInit, QueryList, ViewChildren} from '@angular/core';
import {Board} from '../../../../../api/models/Board';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {BoardPartFormComponent} from '../board-part-form/board-part-form.component';
import {BoardPart} from '../../../../../api/models/BoardPart';
import {FormImpl} from '../../form-impl';
import * as UUID from 'uuid/v4';
import {Project} from '../../../../../api/models/Project';
import {ApiService} from '../../../../../api/services/api.service';
import {isNullOrUndefined} from "util";

@Component({
  selector: 'app-board-base-form',
  templateUrl: './board-base-form.component.html',
  styleUrls: ['./board-base-form.component.css']
})
export class BoardBaseFormComponent extends FormImpl implements OnInit {

  @ViewChildren(BoardPartFormComponent)
  viewChildren: QueryList<BoardPartFormComponent>;

  @Input()
  board: Board;

  formBoard: FormGroup;
  fcName: FormControl;
  fcHighestPriority: FormControl;
  fcStartDev: FormControl;
  fcEndDev: FormControl;
  fcAcceptanceTesting: FormControl;
  fcProjectSelection: FormControl;

  projectAssignedDevTeamIds: number = 1;

  projects: Project[];

  constructor(private api: ApiService) {
    super();
  }

  ngOnInit() {
    this.initFormControl();
    this.initFormGroup();
    this.loadProject();

    this.projectAssignedDevTeamIds = this.getDifferentDevTeamIds(this.board.projects);
  }

  private loadProject(): void {
    this.api.project.getList().subscribe(paging => {
      this.projects = paging.items.filter(project => project.board == null);
    })
  }

  initFormControl(): void {
    this.fcName = new FormControl(this.board.name, Validators.required);
    this.fcName.valueChanges.subscribe(name => this.board.name = name);

    this.fcHighestPriority = new FormControl(this.board.highestPriority, Validators.required);
    this.fcHighestPriority.valueChanges.subscribe(value => {
      if(value != null && this.highestPriorityBoardParts.find(item => item.id == value) == null) {
        value = null;
        this.fcHighestPriority.patchValue(value);
      }
      this.board.highestPriority = value
    });

    this.fcStartDev = new FormControl(this.board.startDev, Validators.required);
    this.fcStartDev.valueChanges.subscribe(value => {
      if(value != null && this.startDevBoardParts.find(item => item.id == value) == null) {
        value = null;
        this.fcStartDev.patchValue(value);
      }
      this.board.startDev = value
    });

    this.fcEndDev = new FormControl(this.board.endDev, Validators.required);
    this.fcEndDev.valueChanges.subscribe(value => {
      if(value != null && this.endDevBoardParts.find(item => item.id == value) == null) {
        value = null;
        this.fcEndDev.patchValue(value);
      }
      this.board.endDev = value;
    });

    this.fcAcceptanceTesting = new FormControl(this.board.acceptanceTesting, Validators.required);
    this.fcAcceptanceTesting.valueChanges.subscribe(value => {
      if(value != null && this.acceptanceTestingBoardParts.find(item => item.id == value) == null) {
        value = null;
        this.fcAcceptanceTesting.patchValue(value);
      }
      this.board.acceptanceTesting = value
    });

    this.fcProjectSelection = new FormControl(null);
    this.fcProjectSelection.valueChanges.subscribe(value => {
      if (!isNullOrUndefined(value)) {
        this.addProject(value);
        this.fcProjectSelection.patchValue(null);
      }
    });
  }

  addProject(projectId: string) {
    let p = this.projects.find(value => value.id == projectId);

    if(!Array.isArray(this.board.projects)) this.board.projects = [];
    this.board.projects.push(p);

    this.projects = this.projects.filter(value => value.id != projectId);
    this.projectAssignedDevTeamIds = this.getDifferentDevTeamIds(this.board.projects);
  }

  removeProject(projectId: string) {
    let p = this.board.projects.find(value => value.id == projectId);
    this.projects.push(p);
    this.board.projects = this.board.projects.filter(value => value.id != projectId);
    this.projectAssignedDevTeamIds = this.getDifferentDevTeamIds(this.board.projects);
  }

  initFormGroup(): void {
    this.formBoard = new FormGroup({
      name: this.fcName,
      highestPriority: this.fcHighestPriority,
      startDev: this.fcStartDev,
      endDev: this.fcEndDev,
      acceptanceTesting: this.fcAcceptanceTesting,
      projectSelection: this.fcProjectSelection
    });
  }

  private getDifferentDevTeamIds(projects: Project[]): number {
    let devTeamIds = new Map<string, boolean>();

    if(Array.isArray(projects)) {
      projects.forEach(value => {
        devTeamIds.set(value.devTeam.id, true);
      });
    }

    return devTeamIds.size;
  }

  isValid(): boolean {
    this.validateForm(this.formBoard);
    if(this.formBoard.valid) {
      this.viewChildren.forEach(item => {
        if(!item.isValid()) {
          return false;
        }
      });
      return true;
    } else {
      return false;
    }
  }

  private createChild(): BoardPart {
    let bp = new BoardPart();
    bp.id = UUID();
    bp.name = `Column ${this.board.boardParts.length}`;
    bp.board = this.board;
    bp.maxWip = 0;
    bp.orderIndex = 0;
    bp.leaf = true;
    return bp;
  }

  addBoardPart(): void {
    if(!Array.isArray(this.board.boardParts)) {
      this.board.boardParts = [];
    }
    this.board.boardParts.splice(0,0, this.createChild());
  }

  handleOnAddRight(orderIndex: number): void {
    this.board.boardParts.splice(orderIndex+1,0, this.createChild());
    this.setOrderIndexes();
  }

  handleOnAddLeft(orderIndex: number): void {
    this.board.boardParts.splice(orderIndex,0, this.createChild());
    this.setOrderIndexes();
  }

  handleOnDeleteChild(orderIndex: number): void {
    this.board.boardParts.splice(orderIndex, 1);
    this.setOrderIndexes();
  }

  private setOrderIndexes(): void {
    for(let i=0; i<this.board.boardParts.length; i++){
      this.board.boardParts[i].orderIndex = i;
    }
  }

  private buildAllBoardParts(boardParts: BoardPart[], ignore: string[]=[]): BoardPart[] {
    let bp = [];
    if(boardParts != null) {
      boardParts.forEach(value => {
        if(value.children && value.children.length > 0) {
          let cBp = this.buildAllBoardParts(value.children, ignore);
          bp = bp.concat(cBp)
        } else {
          if(!ignore.includes(value.id)) {
            bp.push(value);
          }
        }
      });
    }
    return bp;
  }

  get highestPriorityBoardParts(): BoardPart[] {
    let ignore = [this.board.endDev, this.board.startDev, this.board.acceptanceTesting];
    return this.buildAllBoardParts(this.board.boardParts, ignore);
  }

  get acceptanceTestingBoardParts(): BoardPart[] {
    let ignore = [this.board.endDev, this.board.startDev, this.board.highestPriority];
    return this.buildAllBoardParts(this.board.boardParts, ignore);
  }

  get startDevBoardParts(): BoardPart[] {
    let ignore = [this.board.endDev, this.board.highestPriority, this.board.acceptanceTesting];
    return this.buildAllBoardParts(this.board.boardParts, ignore);
  }

  get endDevBoardParts(): BoardPart[] {
    let ignore = [this.board.highestPriority, this.board.startDev, this.board.acceptanceTesting];
    return this.buildAllBoardParts(this.board.boardParts, ignore);
  }

}
