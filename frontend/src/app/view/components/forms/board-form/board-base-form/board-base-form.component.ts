import {Component, EventEmitter, Input, OnInit, Output, QueryList, ViewChildren} from '@angular/core';
import {Board} from '../../../../../api/models/Board';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {BoardPartFormComponent} from '../board-part-form/board-part-form.component';
import {BoardPart} from '../../../../../api/models/BoardPart';
import {FormImpl} from '../../form-impl';

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

  constructor() {
    super();
  }

  ngOnInit() {
    this.initFormControl();
    this.initFormGroup();
  }

  initFormControl(): void {
    this.fcName = new FormControl(this.board.name, Validators.required);
    this.fcName.valueChanges.subscribe(name => this.board.name = name);

    this.fcHighestPriority = new FormControl(this.board.highestPriority, Validators.required);
    this.fcHighestPriority.valueChanges.subscribe(value => this.board.highestPriority = value);

    this.fcStartDev = new FormControl(this.board.startDev, Validators.required);
    this.fcStartDev.valueChanges.subscribe(value => this.board.startDev = value);

    this.fcEndDev = new FormControl(this.board.endDev, Validators.required);
    this.fcEndDev.valueChanges.subscribe(value => this.board.endDev = value);

    this.fcAcceptanceTesting = new FormControl(this.board.acceptanceTesting, Validators.required);
    this.fcAcceptanceTesting.valueChanges.subscribe(value => this.board.acceptanceTesting = value);
  }

  initFormGroup(): void {
    this.formBoard = new FormGroup({
      name: this.fcName,
      highestPriority: this.fcHighestPriority,
      startDev: this.fcStartDev,
      endDev: this.fcEndDev,
      acceptanceTesting: this.fcAcceptanceTesting
    });
  }

  isValid(): boolean {
    this.validateForm(this.formBoard);
    if(this.formBoard.valid) {
      this.viewChildren.forEach(item => {
        if(!item.isValid()) {
          return false;
        }
      });
    } else {
      return false;
    }
  }

  private createChild(): BoardPart {
    let bp = new BoardPart();
    bp.name = `-- Column ${this.board.boardParts.length} --`;
    return bp;
  }

  private initArray(): void {
    if(!Array.isArray(this.board.boardParts)) {
      this.board.boardParts = [];
    }
  }

  addRight(): void {
    this.initArray();
    this.board.boardParts.push(this.createChild());
    this.setOrderIndexes();
  }

  addLeft(): void {
    this.initArray();
    this.board.boardParts.splice(0,0, this.createChild());
    this.setOrderIndexes();
  }

  private setOrderIndexes(): void {
    for(let i=0; i<this.board.boardParts.length; i++){
      this.board.boardParts[i].orderIndex = i;
    }
  }

  onDeleteChild(orderIndex: number): void {
    this.board.boardParts.splice(orderIndex, 1);
    this.setOrderIndexes();
  }

}
