import {HttpParams} from '@angular/common/http';

export enum OrderByType {
  ASC, DESC
}

export enum Comperator {
  LIKE = ":like:",
  LIKEIC = ":likeic:",
  EQ = ":eq:",
  EQIC = ":eqic:",
  NEQ = ":neq:",
  NEQIC = ":neqic:",
  GT = ":gt:",
  GEQ = ":gte:",
  LT = ":lt:",
  LEQ = ":lte:",
  IS_NULL = ":isnull",
  IS_NOT_NULL = ":isnotnull"
}

export class QueryBuilder {

  private static AND: string = ',';
  private static OR: string = ',';
  private static PAR: string = "'";


  private _select: Set<string> = new Set();
  private _where: Set<string> = new Set();

  private _orderBy: string = null;
  private _orderByType: OrderByType = OrderByType.ASC;
  private _limit: number = 30;
  private _skip: number = 0;
  private _defaultIsDeleted: boolean;


  constructor(defaultIsDeleted: boolean=false) {
    this._defaultIsDeleted = defaultIsDeleted;
  }

  static query(defaultIsDeleted: boolean=false): QueryBuilder {
    return new QueryBuilder(defaultIsDeleted);
  }

  build(): HttpParams {
    let params = new HttpParams();

    if(this._limit){
      params.append("limit", this._limit.toString());
    }

    if(this._skip){
      params.append("skip", this._skip.toString());
    }

    if(this._orderBy){
      params.append("orderBy", this._orderBy + " " + this._orderByType);
    }

    if(this._select.size > 0){
      let content = '';
      for(let select in this._select){
        content = content + select + ',';
      }
      params.append("select", content);
    }

    if(this._defaultIsDeleted){
      this.eq("isDeleted", "true");
    }

    if(this._where.size > 0){
      let content = '';
      for(let where in this._where){
        content = content + where + ',';
      }
      params.append("where", content);
    }

    return params;
  }

  select(field: string): QueryBuilder{
    this._select.add(field);
    return this;
  }

  like(field: string, value: string): QueryBuilder {
    this.cond(Comperator.LIKE, field, value);
    return this;
  }

  likeic(field: string, value: string): QueryBuilder {
    this.cond(Comperator.LIKEIC, field, value);
    return this;
  }

  eq(field: string, value: string | number): QueryBuilder {
    this.cond(Comperator.EQ, field, value);
    return this;
  }

  neq(field: string, value: string | number): QueryBuilder {
    this.cond(Comperator.NEQ, field, value);
    return this;
  }

  eqic(field: string, value: string): QueryBuilder {
    this.cond(Comperator.EQIC, field, value);
    return this;
  }

  neqic(field: string, value: string): QueryBuilder {
    this.cond(Comperator.NEQIC, field, value);
    return this;
  }

  gt(field: string, value: number): QueryBuilder {
    this.cond(Comperator.GT, field, value);
    return this;
  }

  geq(field: string, value: number): QueryBuilder {
    this.cond(Comperator.GEQ, field, value);
    return this;
  }

  lt(field: string, value: number): QueryBuilder {
    this.cond(Comperator.LT, field, value);
    return this;
  }

  leq(field: string, value: number): QueryBuilder {
    this.cond(Comperator.LEQ, field, value);
    return this;
  }

  isNull(field: string): QueryBuilder {
    this.cond(Comperator.IS_NULL, field);
    return this;
  }

  isNotNull(field: string): QueryBuilder {
    this.cond(Comperator.IS_NOT_NULL, field);
    return this;
  }

  cond(comp: Comperator, field: string, value: string | number=null){
    switch (comp) {
      case Comperator.IS_NULL:
      case Comperator.IS_NOT_NULL:
        this._where.add(field + comp);
        break;
      default:
        if(value != null){
          this._where.add(field + comp + QueryBuilder.PAR + value + QueryBuilder.PAR);
        }
    }
  }

  orderBy(orderBy: string, orderByType: OrderByType=OrderByType.ASC): QueryBuilder {
    this._orderBy = orderBy;
    this._orderByType = orderByType;
    return this;
  }

  limit(value: number): QueryBuilder {
    this._limit = value;
    return this;
  }

  skip(value: number): QueryBuilder {
    this._skip = value;
    return this;
  }

  isDeleted(value: boolean): QueryBuilder {
    this._defaultIsDeleted = value;
    return this;
  }

}
