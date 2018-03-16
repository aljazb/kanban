import {BaseEntity} from './base/BaseEntity';
import {FlowTable} from './FlowTable';
import {Card} from './Card';

export class FlowTablePart extends BaseEntity<FlowTablePart> {
  name: string;
  maxWip: number;

  flowTable: FlowTable;
  parent: FlowTablePart;
  children: FlowTablePart[];
  cards: Card[];
}
