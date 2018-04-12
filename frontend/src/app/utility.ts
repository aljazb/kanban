
export class DTDateFormat {
  year: number;
  month: number;
  day: number;


  constructor(year: number, month: number, day: number) {
    this.year = year;
    this.month = month;
    this.day = day;
  }
}

export function cDpToTs(sp: DTDateFormat){
  if(sp){
    let d = new Date(sp.year, sp.month-1, sp.day, 0, 0, 0, 0);
    return d.getTime();
  } else {
    return null;
  }
}

export function cTsToDp(timestamp: number) : DTDateFormat {
  if(timestamp){
    let d = new Date(timestamp);
    return new DTDateFormat(d.getFullYear(), d.getMonth() + 1, d.getDate());
  } else {
    return null;
  }
}
