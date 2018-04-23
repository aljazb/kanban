import { Component, OnInit } from '@angular/core';
import {PagingImpl, QueryImpl} from '../PagingImpl';
import {DevTeam} from '../../../../api/models/DevTeam';
import {HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {Paging} from '../../../../api/dto/Paging';
import {ApiService} from '../../../../api/services/api.service';
import {QueryBuilder} from '../../../../api/query/query-builder';

@Component({
  selector: 'app-dev-team-paging',
  templateUrl: './dev-team-paging.component.html',
  styleUrls: ['./dev-team-paging.component.css']
})
export class DevTeamPagingComponent extends PagingImpl<DevTeam> implements OnInit {

  constructor(private api:ApiService) {
    super();
    this.pageSize = 8;
  }

  ngOnInit() {

  }

  protected initialQuery(): QueryImpl<DevTeam> {
    return new DevTeamQuery();
  }

  protected getList(params: HttpParams): Observable<Paging<DevTeam>> {
    return this.api.devTeam.getList(params);
  }

}

class DevTeamQuery extends DevTeam implements QueryImpl<DevTeam> {

  constructor() {
    super();
  }

  addQuery(qb: QueryBuilder): QueryBuilder {
    return qb;
  }

}
