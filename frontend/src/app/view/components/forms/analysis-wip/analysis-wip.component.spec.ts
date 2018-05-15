import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AnalysisWipComponent } from './analysis-wip.component';

describe('AnalysisWipComponent', () => {
  let component: AnalysisWipComponent;
  let fixture: ComponentFixture<AnalysisWipComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AnalysisWipComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AnalysisWipComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
