import {Component, EventEmitter, Input, OnInit, Output, QueryList, ViewChildren} from '@angular/core';
import {BoardPart} from '../../../../../api/models/BoardPart';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {FormImpl} from '../../form-impl';
import {v4} from 'uuid';

@Component({
  selector: 'app-board-part-form',
  templateUrl: './board-part-form.component.html',
  styleUrls: ['./board-part-form.component.css']
})
export class BoardPartFormComponent extends FormImpl implements OnInit {

  @ViewChildren(BoardPartFormComponent)
  viewChildren: QueryList<BoardPartFormComponent>;

  @Input()
  boardPart: BoardPart;

  @Output()
  onDelete: EventEmitter<any> = new EventEmitter();

  @Output()
  onAddLeft: EventEmitter<any> = new EventEmitter();

  @Output()
  onAddRight: EventEmitter<any> = new EventEmitter();

  formBoardPart: FormGroup;
  fcName: FormControl;
  fcMaxWip: FormControl;

  constructor() {
    super();
  }

  ngOnInit() {
    this.initFormControl();
    this.initFormGroup();
  }

  initFormControl(): void {
    this.fcName = new FormControl(this.boardPart.name, Validators.required);
    this.fcName.valueChanges.subscribe(name => this.boardPart.name = name);

    this.fcMaxWip = new FormControl(this.boardPart.maxWip, Validators.pattern('([0-9]+)'));
    this.fcMaxWip.valueChanges.subscribe(maxWip => this.boardPart.maxWip = maxWip);
  }

  initFormGroup(): void {
    this.formBoardPart = new FormGroup({
      name: this.fcName,
      maxWip: this.fcMaxWip
    });
  }

  isValid(): boolean {
    this.validateForm(this.formBoardPart);
    if(this.formBoardPart.valid) {
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
    bp.id = v4();
    bp.parent = this.boardPart;
    bp.board = this.boardPart.board;
    bp.maxWip = 0;
    bp.orderIndex = 0;
    bp.name = this.getChildName();
    return bp;
  }

  triggerOnAddRight(): void {
    this.onAddRight.emit(this.boardPart.orderIndex);
  }

  triggerOnAddLeft(): void {
    this.onAddLeft.emit(this.boardPart.orderIndex);
  }

  handleOnAddRight(index: number): void {
    this.boardPart.children.splice(index+1, 0, this.createChild());
    this.setOrderIndexes();
  }

  addDown(): void {
    if(!Array.isArray(this.boardPart.children)){
      this.boardPart.children = [];
    }

    this.boardPart.children.splice(0, 0, this.createChild());
    this.setOrderIndexes();
  }

  handleOnAddLeft(index: number): void {
    this.boardPart.children.splice(index,0, this.createChild());
    this.setOrderIndexes();
  }

  onDeleteChild(orderIndex: number): void {
    this.boardPart.children.splice(orderIndex, 1);
    this.setOrderIndexes();
  }

  delete(): void {
    this.onDelete.emit(this.boardPart.orderIndex);
  }

  private setOrderIndexes(): void {
    for(let i=0; i<this.boardPart.children.length; i++){
      this.boardPart.children[i].orderIndex = i;
    }
  }

  getChildName() {
    return `Column ${this.getName(this.boardPart)}.${this.boardPart.children.length}`;
  }

  getName(boardPart: BoardPart=this.boardPart): string {
    let name = `${boardPart.orderIndex}`;
    let parent = boardPart.parent;
    while(parent) {
      name = `${parent.orderIndex}.` + name;
      parent = parent.parent;
    }
    return name;
  }

  get isAcceptanceTesting(): boolean {
    return this.boardPart.id == this.boardPart.board.acceptanceTesting
  }

  get isHighestPriority(): boolean {
    return this.boardPart.id == this.boardPart.board.highestPriority
  }

  get isStartDev(): boolean {
    return this.boardPart.id == this.boardPart.board.startDev
  }

  get isEndDev(): boolean {
    return this.boardPart.id == this.boardPart.board.endDev;
  }


}

