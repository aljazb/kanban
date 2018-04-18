import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BoardDetailsEditComponent } from './board-details-edit.component';

describe('BoardDetailsEditComponent', () => {
  let component: BoardDetailsEditComponent;
  let fixture: ComponentFixture<BoardDetailsEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BoardDetailsEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BoardDetailsEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
