import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectCreationFormComponent } from './project-creation-form.component';

describe('ProjectCreationFormComponent', () => {
  let component: ProjectCreationFormComponent;
  let fixture: ComponentFixture<ProjectCreationFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectCreationFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectCreationFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
