import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BoardPartFormComponent } from './board-part-form.component';

describe('BoardPartFormComponent', () => {
  let component: BoardPartFormComponent;
  let fixture: ComponentFixture<BoardPartFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BoardPartFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BoardPartFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
