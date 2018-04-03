import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserAccountPasswordFormComponent } from './user-account-password-form.component';

describe('UserAccountPasswordFormComponent', () => {
  let component: UserAccountPasswordFormComponent;
  let fixture: ComponentFixture<UserAccountPasswordFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserAccountPasswordFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserAccountPasswordFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
