import {Membership} from './Membership';
import {BaseEntity} from './base/BaseEntity';

export class HistoryEvent extends BaseEntity<Membership> {
  date: number;
  event: string;

  static getDate(historyEvent: HistoryEvent) {
    return new Date(historyEvent.date);
  }
}
