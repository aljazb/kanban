import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserSelectionFormComponent } from './user-selection-form.component';

describe('UserSelectionFormComponent', () => {
  let component: UserSelectionFormComponent;
  let fixture: ComponentFixture<UserSelectionFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserSelectionFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserSelectionFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
