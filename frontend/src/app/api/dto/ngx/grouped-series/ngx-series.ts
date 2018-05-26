export class NgxSeries {

  name: string;
  value: number;
  ratio: number;

  constructor(name: string, value: number, ratio: number = null) {
    this.name = name;
    this.value = value;
    this.ratio = ratio;
  }

}
