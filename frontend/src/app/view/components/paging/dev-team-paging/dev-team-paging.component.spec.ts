import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DevTeamPagingComponent } from './dev-team-paging.component';

describe('DevTeamPagingComponent', () => {
  let component: DevTeamPagingComponent;
  let fixture: ComponentFixture<DevTeamPagingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DevTeamPagingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DevTeamPagingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
