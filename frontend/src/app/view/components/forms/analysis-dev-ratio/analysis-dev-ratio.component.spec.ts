import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AnalysisDevRatioComponent } from './analysis-dev-ratio.component';

describe('AnalysisDevRatioComponent', () => {
  let component: AnalysisDevRatioComponent;
  let fixture: ComponentFixture<AnalysisDevRatioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AnalysisDevRatioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AnalysisDevRatioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
