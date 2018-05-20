import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CardMoveRulesPagingComponent } from './card-move-rules-paging.component';

describe('CardMoveRulesPagingComponent', () => {
  let component: CardMoveRulesPagingComponent;
  let fixture: ComponentFixture<CardMoveRulesPagingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CardMoveRulesPagingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardMoveRulesPagingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
