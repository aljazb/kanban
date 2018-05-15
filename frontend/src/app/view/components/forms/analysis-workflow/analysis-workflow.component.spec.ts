import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AnalysisWorkflowComponent } from './analysis-workflow.component';

describe('AnalysisWorkflowComponent', () => {
  let component: AnalysisWorkflowComponent;
  let fixture: ComponentFixture<AnalysisWorkflowComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AnalysisWorkflowComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AnalysisWorkflowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
