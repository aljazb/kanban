import {Component, ElementRef, HostListener, Input, OnInit, ViewChild} from '@angular/core';
import {ApiService} from '../../../../api/services/api.service';
import {WipQuery} from '../../../../api/dto/analysis/wip/wip-query';
import {Project} from '../../../../api/models/Project';
import {SharedContext} from '../../../route/analysis/utility/shared-context';
import {WipResponse} from '../../../../api/dto/analysis/wip/wip-response';
import {CardMoveType} from '../../../../api/models/enums/CardMoveType';

@Component({
  selector: 'app-analysis-wip',
  templateUrl: './analysis-wip.component.html',
  styleUrls: ['./analysis-wip.component.css']
})
export class AnalysisWipComponent implements OnInit {

  cmt = CardMoveType;

  @ViewChild('wipContent')
  contentDiv: ElementRef;

  @Input()
  sharedContext: SharedContext;

  response: WipResponse;

  constructor(private api: ApiService) { }

  ngOnInit() {

  }

  handleProjectSelect(project: Project) {

  }

  scrollToContent(): void {
    this.contentDiv.nativeElement.scrollIntoView({ block: 'start',  behavior: 'smooth' });
  }

  submitWip() {
    let query: WipQuery = Object.assign(new WipQuery(), this.sharedContext.query);
    this.api.analysis.getWip(query).subscribe(value => {
      this.response = value;
      setTimeout(() => {this.scrollToContent()}, 300);
    });
  }

}
