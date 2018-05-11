import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CardMovesPagingComponent } from './card-moves-paging.component';

describe('CardMovesPagingComponent', () => {
  let component: CardMovesPagingComponent;
  let fixture: ComponentFixture<CardMovesPagingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CardMovesPagingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardMovesPagingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
