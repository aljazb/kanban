import {Component, Input, OnInit, QueryList, ViewChildren} from '@angular/core';
import {BoardPart} from '../../../../../api/models/BoardPart';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-board-part-form',
  templateUrl: './board-part-form.component.html',
  styleUrls: ['./board-part-form.component.css']
})
export class BoardPartFormComponent implements OnInit {

  @ViewChildren(BoardPartFormComponent)
  viewChildren: QueryList<BoardPartFormComponent>;

  @Input()
  boardPart: BoardPart;

  formBoardPart: FormGroup;
  fcName: FormControl;
  fcMaxWip: FormControl;

  constructor() { }

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
}

