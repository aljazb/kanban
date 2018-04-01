import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectDeleteConfirmationComponent } from './project-delete-confirmation.component';

describe('ProjectDeleteConfirmationComponent', () => {
  let component: ProjectDeleteConfirmationComponent;
  let fixture: ComponentFixture<ProjectDeleteConfirmationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectDeleteConfirmationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectDeleteConfirmationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
