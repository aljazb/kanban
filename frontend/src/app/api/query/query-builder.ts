import {HttpParams} from '@angular/common/http';

export enum OrderByType {
  ASC, DESC
}

export enum Comperator {
  EQ, GEQ, LEQ
}

export class QueryBuilder {

  private static AND: string = ',';
  private static OR: string = ',';

  public static LIKE: string = ":like:";
  public static LIKEIC: string = ":likeic:";
  public static EQ: string = ":eq:";
  public static GEQ: string = ":gte:";
  public static LEQ: string = ":lte:";
  public static IS_NULL: string = ":isnull";
  public static IS_NOT_NULL: string = ":isnotnull";

  public static PAR: string = "'";


  public select: string[] = [];
  public where: string[] = [];
  public orderBy: string = null;
  public orderByType: OrderByType = OrderByType.ASC;

  public limit: number = 30;
  public skip: number = 0;
  private isDeleted: boolean = false;


  build(): HttpParams {
    let params = new HttpParams();

    if(this.select.length > 0) {
      let select = '';
      for(let select in this.select){

      }
    }



    return params;
  }

}
