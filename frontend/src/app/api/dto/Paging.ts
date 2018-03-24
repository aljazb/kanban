import {BaseEntity} from '../models/base/BaseEntity';

export class Paging<T extends BaseEntity<T>> {
  count: number;
  items: T[];


  constructor(count: number, items: T[]) {
    this.count = count;
    this.items = items;
  }
}
