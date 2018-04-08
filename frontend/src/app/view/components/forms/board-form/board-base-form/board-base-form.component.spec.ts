import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BoardBaseFormComponent } from './board-base-form.component';

describe('BoardBaseFormComponent', () => {
  let component: BoardBaseFormComponent;
  let fixture: ComponentFixture<BoardBaseFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BoardBaseFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BoardBaseFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
