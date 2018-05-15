import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AnalysisQueryComponent } from './analysis-query.component';

describe('AnalysisQueryComponent', () => {
  let component: AnalysisQueryComponent;
  let fixture: ComponentFixture<AnalysisQueryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AnalysisQueryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AnalysisQueryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
