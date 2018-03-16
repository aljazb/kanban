import {BaseEntity} from './base/BaseEntity';
import {DevTeam} from './DevTeam';
import {FlowTablePart} from './FlowTablePart';
import {Project} from './Project';

export class FlowTable extends BaseEntity<FlowTable> {
  name: string;
  devTeam: DevTeam;
  rootFlowTablePart: FlowTablePart;
  projects: Project[];
}
