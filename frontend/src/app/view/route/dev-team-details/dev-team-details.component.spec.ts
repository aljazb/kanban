import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DevTeamDetailsComponent } from './dev-team-details.component';

describe('DevTeamDetailsComponent', () => {
  let component: DevTeamDetailsComponent;
  let fixture: ComponentFixture<DevTeamDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DevTeamDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DevTeamDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
