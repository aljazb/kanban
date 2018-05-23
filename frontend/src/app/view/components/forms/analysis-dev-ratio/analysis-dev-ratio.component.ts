import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {ApiService} from '../../../../api/services/api.service';
import {SharedContext} from '../../../route/analysis/utility/shared-context';
import {DeveloperRatioQuery} from '../../../../api/dto/analysis/devRatio/developer-ratio-query';
import {DeveloperRatioResponse} from '../../../../api/dto/analysis/devRatio/developer-ratio-response';
import {NgxSeries} from '../../../../api/dto/ngx/grouped-series/ngx-series';

@Component({
  selector: 'app-analysis-dev-ratio',
  templateUrl: './analysis-dev-ratio.component.html',
  styleUrls: ['./analysis-dev-ratio.component.css']
})
export class AnalysisDevRatioComponent implements OnInit {

  @Input()
  sharedContext: SharedContext;

  @ViewChild('devRatioContent')
  contentDiv: ElementRef;

  ngxDataSetWorkload: NgxSeries[];
  ngxDataSetCards: NgxSeries[];

  response: DeveloperRatioResponse;

  constructor(private api: ApiService) { }

  ngOnInit() {
  }

  submitDevRatio() {

    let query: DeveloperRatioQuery = Object.assign(new DeveloperRatioQuery(), this.sharedContext.query);
    this.api.analysis.getDevRatio(query).subscribe(value => {
      this.handleResponse(value);
    });

  }

  scrollToContent(): void {
    console.log(this.contentDiv);
    this.contentDiv.nativeElement.scrollIntoView({ block: 'start',  behavior: 'smooth' });
  }

  private handleResponse(response: DeveloperRatioResponse): void {
    this.response = response;
    if(response.data) {
      let workload: NgxSeries[] = [];
      let cards: NgxSeries[] = [];

      response.data.forEach(value => {
        workload.push(new NgxSeries(value.developer.email, value.combinedWorkload));
        cards.push(new NgxSeries(value.developer.email, value.combinedCards));
      });

      this.ngxDataSetWorkload = workload;
      this.ngxDataSetCards = cards;

      setTimeout(() => {this.scrollToContent()}, 300);
    }
  }

}
