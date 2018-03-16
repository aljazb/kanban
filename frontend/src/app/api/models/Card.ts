import {BaseEntity} from './base/BaseEntity';
import {Project} from './Project';
import {FlowTablePart} from './FlowTablePart';

export class Card extends BaseEntity<Card> {
  name: string;
  description: string;
  workload: number;

  project: Project;
  flowTablePart: FlowTablePart;
}
