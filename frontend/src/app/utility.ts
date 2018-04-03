import {Toast} from 'angular5-toaster/dist';


export class Utility {

  public static cDpToTs(sp: { year:number, month: number, day: number }){
    if(sp){
      let d = new Date(sp.year, sp.month-1, sp.day, 0, 0, 0, 0);
      return d.getTime();
    } else {
      return null;
    }
  }

  public static cTsToDp(timestamp: number) : { year:number, month: number, day: number }{
    if(timestamp){
      let d = new Date(timestamp);
      return {year: d.getFullYear(), month: d.getMonth()+1, day: d.getDate()};
    } else {
      return null;
    }
  }
}




