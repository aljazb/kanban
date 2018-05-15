import {Component, Input, OnInit, QueryList, ViewChildren} from '@angular/core';
import {Board} from '../../../../../api/models/Board';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {BoardPartFormComponent} from '../board-part-form/board-part-form.component';
import {BoardPart} from '../../../../../api/models/BoardPart';
import {FormImpl} from '../../form-impl';
import * as UUID from 'uuid/v4';
import {Project} from '../../../../../api/models/Project';
import {ApiService} from '../../../../../api/services/api.service';
import {BoardEvent, BoardEventType} from '../utility/board-event';

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

  leafBoardParts: BoardPart[];

  formTime: FormGroup;
  fcName: FormControl;

  fcHighestPrioritySelection: BoardPart[];
  fcHighestPriority: FormControl;

  fcStartDevSelection: BoardPart[];
  fcStartDev: FormControl;

  fcEndDevSelection: BoardPart[];
  fcEndDev: FormControl;

  fcAcceptanceTestingSelection: BoardPart[];
  fcAcceptanceTesting: FormControl;

  fcProjectsSelection: Project[];
  fcProject: FormControl;

  projectAssignedDevTeamIds: number = 1;

  constructor(private api: ApiService) {
    super();
  }

  ngOnInit() {
    this.updateLeafs();
    this.updateSelection();

    this.initFormControl();
    this.initFormGroup();
    this.loadProject();
  }

  private loadProject(): void {
    this.api.project.getList().subscribe(paging => {
      this.fcProjectsSelection = paging.items.filter(project => project.board == null);
      this.projectAssignedDevTeamIds = this.getDifferentDevTeamIds(this.board.projects);
    })
  }

  private initFormControl(): void {
    this.fcName = new FormControl(this.board.name, Validators.required);
    this.fcName.valueChanges.subscribe(name => this.board.name = name);

    this.fcHighestPriority = new FormControl(this.getLeafId(this.board.highestPriority), Validators.required);
    this.fcHighestPriority.valueChanges.subscribe(value => {
      if(value == null) {
        this.board.highestPriority = null;
      } else {
        let bp = this.leafBoardParts.find(item => item.id == value);
        if(bp == null) {
          this.fcHighestPriority.patchValue(null);
        } else {
          this.board.highestPriority = bp.leafNumber;
        }
      }
      this.updateSelection();
    });

    this.fcStartDev = new FormControl(this.getLeafId(this.board.startDev), Validators.required);
    this.fcStartDev.valueChanges.subscribe(value => {
      if(value == null) {
        this.board.startDev = null;
      } else {
        let bp = this.leafBoardParts.find(item => item.id == value);
        if(bp == null) {
          this.fcStartDev.patchValue(null);
        } else {
          this.board.startDev = bp.leafNumber;
        }
      }
      this.updateSelection();
    });

    this.fcEndDev = new FormControl(this.getLeafId(this.board.endDev), Validators.required);
    this.fcEndDev.valueChanges.subscribe(value => {
      if(value == null) {
        this.board.endDev = null;
      } else {
        let bp = this.leafBoardParts.find(item => item.id == value);
        if(bp == null) {
          this.fcEndDev.patchValue(null);
        } else {
          this.board.endDev = bp.leafNumber;
        }
      }
      this.updateSelection();
    });

    this.fcAcceptanceTesting = new FormControl(this.getLeafId(this.board.acceptanceTesting), Validators.required);
    this.fcAcceptanceTesting.valueChanges.subscribe(value => {
      if(value == null) {
        this.board.acceptanceTesting = null;
      } else {
        let bp = this.leafBoardParts.find(item => item.id == value);
        if(bp == null) {
          this.fcAcceptanceTesting.patchValue(null);
        } else {
          this.board.acceptanceTesting = bp.leafNumber;
        }
      }
      this.updateSelection();
    });

    this.fcProject = new FormControl(null);
    this.fcProject.valueChanges.subscribe(value => {
      if (value != null) {
        this.addProject(value);
        this.fcProject.patchValue(null);
      }
    });
  }

  private initFormGroup(): void {
    this.formTime = new FormGroup({
      name: this.fcName,
      highestPriority: this.fcHighestPriority,
      startDev: this.fcStartDev,
      endDev: this.fcEndDev,
      acceptanceTesting: this.fcAcceptanceTesting,
      projectSelection: this.fcProject
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

  private createChild(): BoardPart {
    let bp = new BoardPart();
    bp.id = UUID();
    bp.name = `Column ${this.board.boardParts.length}`;
    bp.board = this.board;
    bp.maxWip = 0;
    bp.orderIndex = 0;
    return bp;
  }

  private getLeafId(number: number): string {
    let bp = this.leafBoardParts.find(value => value.leafNumber == number);
    if(bp == null){
      return null;
    } else {
      return bp.id;
    }
  }

  isValid(): boolean {
    this.validateForm(this.formTime);
    if(this.formTime.valid) {
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

  addProject(projectId: string) {
    let p = this.fcProjectsSelection.find(value => value.id == projectId);

    if(!Array.isArray(this.board.projects)) this.board.projects = [];
    this.board.projects.push(p);

    this.fcProjectsSelection = this.fcProjectsSelection.filter(value => value.id != projectId);
    this.projectAssignedDevTeamIds = this.getDifferentDevTeamIds(this.board.projects);
  }

  removeProject(projectId: string) {
    let p = this.board.projects.find(value => value.id == projectId);
    this.fcProjectsSelection.push(p);
    this.board.projects = this.board.projects.filter(value => value.id != projectId);
    this.projectAssignedDevTeamIds = this.getDifferentDevTeamIds(this.board.projects);
  }

  addInitialBoardPart(): void {
    if(!Array.isArray(this.board.boardParts)) {
      this.board.boardParts = [];
    }
    this.board.boardParts.splice(0,0, this.createChild());

    this.updateStructure();
  }

  handleOnEvent(event: BoardEvent): void {
    switch (event.type) {
      case BoardEventType.EVENT_ADD_LEFT: this.addLeft(event.value); break;
      case BoardEventType.EVENT_ADD_RIGHT: this.addRight(event.value); break;
      case BoardEventType.EVENT_DELETE: this.deleteChild(event.value); break;
      case BoardEventType.EVENT_STRUCTURE_CHANGED: this.updateStructure(); break;
    }

  }

  private updateStructure(): void {
    this.setOrderIndexes();
    this.updateLeafs();
    this.refreshForm();
    this.updateSelection();
  }

  private setOrderIndexes(): void {
    for(let i=0; i<this.board.boardParts.length; i++){
      this.board.boardParts[i].orderIndex = i;
    }
  }

  private updateLeafs(): void {
    this.leafBoardParts = Board.getLeafParts(this.board.boardParts);
    for(let i=0; i<this.leafBoardParts.length; i++) {
      this.leafBoardParts[i].leafNumber = i;
    }
  }

  private refreshForm(): void {
    this.fcHighestPriority.patchValue(this.fcHighestPriority.value);
    this.fcStartDev.patchValue(this.fcStartDev.value);
    this.fcEndDev.patchValue(this.fcEndDev.value);
    this.fcAcceptanceTesting.patchValue(this.fcAcceptanceTesting.value);
  }

  private updateSelection (){
    this.fcHighestPrioritySelection = this.buildAllBoardParts(this.leafBoardParts, this.getStartEndIndex(0));
    this.fcStartDevSelection = this.buildAllBoardParts(this.leafBoardParts, this.getStartEndIndex(1));
    this.fcEndDevSelection = this.buildAllBoardParts(this.leafBoardParts, this.getStartEndIndex(2));
    this.fcAcceptanceTestingSelection = this.buildAllBoardParts(this.leafBoardParts, this.getStartEndIndex(3));
  }

  private getStartEndIndex(index: number): { start:number, end: number } {
    let constr = [this.board.highestPriority, this.board.startDev, this.board.endDev, this.board.acceptanceTesting];

    let start = null;
    let end = null;

    for(let i=index-1; i>=0; i--){
      if(constr[i] != null) {
        start = constr[i];
        break;
      }
    }

    for(let i=index+1; i<constr.length; i++){
      if(constr[i] != null) {
        end = constr[i];
        break;
      }
    }

    return { start: start, end: end };
  }

  private buildAllBoardParts(boardParts: BoardPart[], index: { start: number, end: number }): BoardPart[] {
    let bp = [];
    if(boardParts != null) {
      boardParts.forEach(boardPart => {
        if(index.start == null && index.end == null){
          bp.push(boardPart);
        } else if(index.start == null){
          if(boardPart.leafNumber < index.end) {
            bp.push(boardPart);
          }
        } else if(index.end == null){
          if(boardPart.leafNumber > index.start) {
            bp.push(boardPart);
          }
        } else {
          if(boardPart.leafNumber > index.start && boardPart.leafNumber < index.end) {
            bp.push(boardPart);
          }
        }
      });
    }
    return bp;
  }

  private addRight(orderIndex: number): void {
    this.board.boardParts.splice(orderIndex+1,0, this.createChild());
    this.updateStructure();
  }

  private addLeft(orderIndex: number): void {
    this.board.boardParts.splice(orderIndex,0, this.createChild());
    this.updateStructure();
  }

  private deleteChild(orderIndex: number): void {
    this.board.boardParts.splice(orderIndex, 1);
    this.updateStructure();
  }

}
