import {Component, Input, OnInit, QueryList, ViewChildren} from '@angular/core';
import {Board} from '../../../../../api/models/Board';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {BoardPartFormComponent} from '../board-part-form/board-part-form.component';

@Component({
  selector: 'app-board-base-form',
  templateUrl: './board-base-form.component.html',
  styleUrls: ['./board-base-form.component.css']
})
export class BoardBaseFormComponent implements OnInit {

  @ViewChildren(BoardPartFormComponent)
  viewChildren: QueryList<BoardPartFormComponent>;

  @Input()
  board: Board;

  formBoard: FormGroup;
  fcName: FormControl;

  constructor() { }

  ngOnInit() {
    this.initFormControl();
    this.initFormGroup();
  }

  initFormControl(): void {
    this.fcName = new FormControl(this.board.name, Validators.required);
    this.fcName.valueChanges.subscribe(name => {
      this.board.name = name;
      console.log(this.board);
    });
  }

  initFormGroup(): void {
    this.formBoard = new FormGroup({
      name: this.fcName
    });
  }

  isValid(): boolean {
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
}
