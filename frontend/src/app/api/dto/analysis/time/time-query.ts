import {AnalysisQuery} from '../analysis-query';
import {BoardPart} from '../../../models/BoardPart';

export class TimeQuery extends AnalysisQuery{

  from: BoardPart;
  to: BoardPart;

}
