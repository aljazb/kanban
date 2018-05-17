import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CardDeleteConfirmationComponent } from './card-delete-confirmation.component';

describe('CardDeleteConfirmationComponent', () => {
  let component: CardDeleteConfirmationComponent;
  let fixture: ComponentFixture<CardDeleteConfirmationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CardDeleteConfirmationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardDeleteConfirmationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
