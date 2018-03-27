

export class Utility {

  public static cDpToTs(sp: { year:number, month: number, day: number }){
    if(sp){
      let d = new Date(sp.year, sp.month, sp.day);
      return d.getTime() / 1000;
    } else {
      return null;
    }
  }

  public static cTsToDp(timestamp: number) : { year:number, month: number, day: number }{
    if(timestamp){
      let d = new Date(timestamp);
      return {year: d.getFullYear(), month: d.getMonth(), day: d.getDay()};
    } else {
      return null;
    }
  }

}


