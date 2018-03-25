import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BoardPagingComponent } from './board-paging.component';

describe('BoardPagingComponent', () => {
  let component: BoardPagingComponent;
  let fixture: ComponentFixture<BoardPagingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BoardPagingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BoardPagingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
