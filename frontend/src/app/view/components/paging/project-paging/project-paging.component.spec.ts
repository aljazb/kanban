import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectPagingComponent } from './project-paging.component';

describe('ProjectPagingComponent', () => {
  let component: ProjectPagingComponent;
  let fixture: ComponentFixture<ProjectPagingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectPagingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectPagingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
