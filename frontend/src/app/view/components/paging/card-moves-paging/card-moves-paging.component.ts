import {Component, Input, OnInit} from '@angular/core';
import {CardMove} from '../../../../api/models/card-move';
import {PagingImpl, QueryImpl} from '../PagingImpl';
import {HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {Paging} from '../../../../api/dto/Paging';
import {DevTeam} from '../../../../api/models/DevTeam';
import {QueryBuilder} from '../../../../api/query/query-builder';
import {of} from 'rxjs/observable/of';
import {CardMoveType} from '../../../../api/models/enums/CardMoveType';

@Component({
  selector: 'app-card-moves-paging',
  templateUrl: './card-moves-paging.component.html',
  styleUrls: ['./card-moves-paging.component.css']
})
export class CardMovesPagingComponent extends PagingImpl<CardMove> implements OnInit {

  cmt = CardMoveType;

  @Input()
  moves: CardMove[];


  constructor() {
    super();
  }

  ngOnInit() {
  }

  protected initialQuery(): QueryImpl<CardMove> {
    return new CardMoveQuery();
  }

  protected getList(params: HttpParams): Observable<Paging<CardMove>> {
    let skip: number = Number(params.get('skip'));
    let limit: number = Number(params.get('limit'));

    if(this.moves) {
      let items = this.moves.slice(skip, skip + limit);
      let paging: Paging<CardMove> = new Paging<CardMove>(this.moves.length, items);
      return of(paging);
    } else {
      let paging: Paging<CardMove> = new Paging<CardMove>(0, []);
      return of(paging);
    }
  }
}

class CardMoveQuery extends DevTeam implements QueryImpl<CardMove> {

  constructor() {
    super();
  }

  addQuery(qb: QueryBuilder): QueryBuilder {
    return qb;
  }

}
