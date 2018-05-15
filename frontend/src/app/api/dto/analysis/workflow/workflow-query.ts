import {AnalysisQuery} from '../analysis-query';
import {BoardPart} from '../../../models/BoardPart';

export class WorkflowQuery extends AnalysisQuery {

  leafBoardParts: BoardPart[];

  constructor() {
    super();
    this.leafBoardParts = [];
  }

}
