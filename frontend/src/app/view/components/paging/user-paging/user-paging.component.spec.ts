import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserPagingComponent } from './user-paging.component';

describe('UserPagingComponent', () => {
  let component: UserPagingComponent;
  let fixture: ComponentFixture<UserPagingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserPagingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserPagingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
