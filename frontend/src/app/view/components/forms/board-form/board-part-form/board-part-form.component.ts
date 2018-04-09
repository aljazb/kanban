import {Component, EventEmitter, Input, OnInit, Output, QueryList, ViewChildren} from '@angular/core';
import {BoardPart} from '../../../../../api/models/BoardPart';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {FormImpl} from '../../form-impl';

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

    this.fcMaxWip = new FormControl(this.boardPart.maxWip, Validators.pattern('(^$|[0-9]+)'));
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
    bp.parent = this.boardPart;
    return bp;
  }

  private initArray(): void {
    if(!Array.isArray(this.boardPart.children)){
      this.boardPart.children = [];
    }
  }

  addRight(): void {
    this.initArray();
    this.boardPart.children.push(this.createChild());
    this.setOrderIndexes();
  }

  addLeft(): void {
    this.initArray();
    this.boardPart.children.splice(0,0, this.createChild());
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

  getName(): string {
    let name = `${this.boardPart.orderIndex}`;
    let parent = this.boardPart.parent;
    while(parent) {
      name = `${parent.orderIndex}.` + name;
      parent = parent.parent;
    }
    return name;
  }

}

