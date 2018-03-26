import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectEditFormComponent } from './project-edit-form.component';

describe('ProjectCreationFormComponent', () => {
  let component: ProjectEditFormComponent;
  let fixture: ComponentFixture<ProjectEditFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectEditFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectEditFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
