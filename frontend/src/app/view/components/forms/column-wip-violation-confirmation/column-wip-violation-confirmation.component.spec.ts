import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ColumnWipViolationConfirmationComponent } from './column-wip-violation-confirmation.component';

describe('ColumnWipViolationConfirmationComponent', () => {
  let component: ColumnWipViolationConfirmationComponent;
  let fixture: ComponentFixture<ColumnWipViolationConfirmationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ColumnWipViolationConfirmationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ColumnWipViolationConfirmationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
