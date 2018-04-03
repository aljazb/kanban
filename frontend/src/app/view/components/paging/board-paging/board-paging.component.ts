import { Component, OnInit } from '@angular/core';
import {PagingImpl, QueryImpl} from '../PagingImpl';
import {Board} from '../../../../api/models/Board';
import {ApiService} from '../../../../api/api.service';
import {Paging} from '../../../../api/dto/Paging';
import {Observable} from 'rxjs/Observable';
import {HttpParams} from '@angular/common/http';
import {DevTeam} from '../../../../api/models/DevTeam';
import {QueryBuilder} from '../../../../api/query/query-builder';

@Component({
  selector: 'app-board-paging',
  templateUrl: './board-paging.component.html',
  styleUrls: ['./board-paging.component.css']
})
export class BoardPagingComponent extends PagingImpl<Board>  implements OnInit {

  constructor(private api:ApiService) {
    super();
    this.pageSize = 8;
  }

  ngOnInit() {
  }

  protected initialQuery(): QueryImpl<Board> {
    return new BoardQuery();
  }

  protected getList(params: HttpParams): Observable<Paging<Board>> {
    return this.api.board.getList(params);
  }
}

class BoardQuery extends DevTeam implements QueryImpl<Board> {

  constructor() {
    super();
  }

  addQuery(qb: QueryBuilder): QueryBuilder {
    return qb;
  }

}
