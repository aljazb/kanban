export class BoardPartTable {
  name: string;
  wip: number;

  children: BoardPartTable[];

  colspan: number;
  rowspan: number;

  constructor(name: string, wip: number) {
    this.name = name;
    this.wip = wip;
  }
}
