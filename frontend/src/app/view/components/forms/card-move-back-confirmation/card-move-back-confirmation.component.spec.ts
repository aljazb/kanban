import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CardMoveBackConfirmationComponent } from './card-move-back-confirmation.component';

describe('CardMoveBackConfirmationComponent', () => {
  let component: CardMoveBackConfirmationComponent;
  let fixture: ComponentFixture<CardMoveBackConfirmationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CardMoveBackConfirmationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardMoveBackConfirmationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
