import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CardMoveConfirmationComponent } from './card-move-confirmation.component';

describe('CardMoveConfirmationComponent', () => {
  let component: CardMoveConfirmationComponent;
  let fixture: ComponentFixture<CardMoveConfirmationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CardMoveConfirmationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardMoveConfirmationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
