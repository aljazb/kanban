import {Component, EventEmitter, Input, OnInit, Output, QueryList, ViewChildren} from '@angular/core';
import {BoardPart} from '../../../../../api/models/BoardPart';
import {AbstractControl, FormControl, FormGroup, ValidatorFn, Validators} from '@angular/forms';
import {FormImpl} from '../../form-impl';
import * as UUID from 'uuid/v4';

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

  @Output()
  onHasCards: EventEmitter<any> = new EventEmitter();


  formBoardPart: FormGroup;
  fcName: FormControl;
  fcMaxWip: FormControl;

  hasCards = false;

  constructor() {
    super();
  }

  ngOnInit() {
    this.initFormControl();
    this.initFormGroup();
    this.updateIsDeletable();
  }

  private updateIsDeletable(){
    if(this.boardPart.cards && this.boardPart.cards.length > 0) {
      this.handleOnHasCards(true);
    }
  }

  initFormControl(): void {
    this.fcName = new FormControl(this.boardPart.name, Validators.required);
    this.fcName.valueChanges.subscribe(name => this.boardPart.name = name);

    this.fcMaxWip = new FormControl(this.boardPart.maxWip, [Validators.pattern('([0-9]+)'), this.checkWip()]);
    this.fcMaxWip.valueChanges.subscribe(maxWip => {
      this.boardPart.maxWip = maxWip;
      this.viewChildren.forEach(item => {
        item.fcMaxWip.patchValue(item.fcMaxWip.value);
        item.fcMaxWip.markAsTouched();
      });
    });
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
      return true;
    } else {
      return false;
    }
  }

  private handleOnHasCards(enabled) {
    if (this.hasCards != enabled) {
      this.hasCards = enabled;
      this.onHasCards.emit(enabled);
    }
  }

  private createChild(): BoardPart {
    let bp = new BoardPart();
    bp.id = UUID();
    bp.parent = this.boardPart;
    bp.board = this.boardPart.board;
    bp.maxWip = this.boardPart.maxWip;
    bp.orderIndex = 0;
    bp.leaf = true;
    bp.name = this.getChildName();
    return bp;
  }
  addDown(): void {
    if(!Array.isArray(this.boardPart.children)){
      this.boardPart.children = [];
    }
    this.boardPart.leaf = false;

    this.boardPart.children.splice(0, 0, this.createChild());
    this.setOrderIndexes();
  }

  triggerOnAddRight(): void {
    this.onAddRight.emit(this.boardPart.orderIndex);
  }

  handleOnAddRight(orderIndex: number): void {
    this.boardPart.children.splice(orderIndex+1, 0, this.createChild());
    this.setOrderIndexes();
  }

  triggerOnAddLeft(): void {
    this.onAddLeft.emit(this.boardPart.orderIndex);
  }

  handleOnAddLeft(orderIndex: number): void {
    this.boardPart.children.splice(orderIndex,0, this.createChild());
    this.setOrderIndexes();
  }

  triggerOnDelete(): void {
    this.onDelete.emit(this.boardPart.orderIndex);
  }

  handleOnDelete(orderIndex: number): void {
    this.boardPart.children.splice(orderIndex, 1);
    this.setOrderIndexes();

    if(this.boardPart.children.length == 0) {
      this.boardPart.children = null;
      this.boardPart.leaf = true;
    }
  }

  private setOrderIndexes(): void {
    for(let i=0; i<this.boardPart.children.length; i++){
      this.boardPart.children[i].orderIndex = i;
    }
  }

  private getChildName() {
    return `Column ${this.getName(this.boardPart)}.${this.boardPart.children.length}`;
  }

  private getName(boardPart: BoardPart=this.boardPart): string {
    let name = `${boardPart.orderIndex}`;
    let parent = boardPart.parent;
    while(parent) {
      name = `${parent.orderIndex}.` + name;
      parent = parent.parent;
    }
    return name;
  }

  private checkWip(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      let maxWip: number = control.value;
      let parent = this.boardPart.parent;

      while(parent) {
        if(!this.wipValid(parent.maxWip, maxWip)) {
          return {'greaterThanParent': {value: control.value}};
        }
        parent = parent.parent;
      }

      return null;
    };
  }

  private wipValid(parent: number, child: number): boolean {
    if(parent == 0) {
      return true;
    } else if (child == 0) {
      return false;
    } else {
      return Number(parent) >= Number(child);
    }
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

  get hasChildren(): boolean {
    return this.boardPart.children != null && this.boardPart.children.length > 0;
  }

}


