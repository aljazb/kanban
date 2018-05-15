import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AnalysisTimeComponent } from './analysis-time.component';

describe('AnalysisTimeComponent', () => {
  let component: AnalysisTimeComponent;
  let fixture: ComponentFixture<AnalysisTimeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AnalysisTimeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AnalysisTimeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
