import { Component, OnInit } from '@angular/core';
import {PagingImpl, QueryImpl} from '../PagingImpl';
import {HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {Paging} from '../../../../api/dto/Paging';
import {Project} from '../../../../api/models/Project';
import {QueryBuilder} from '../../../../api/query/query-builder';
import {ApiService} from '../../../../api/api.service';

@Component({
  selector: 'app-project-paging',
  templateUrl: './project-paging.component.html',
  styleUrls: ['./project-paging.component.css']
})
export class ProjectPagingComponent extends PagingImpl<Project> implements OnInit {

  constructor(private api:ApiService) {
    super();
    this.pageSize = 8;
  }

  ngOnInit() {
  }

  protected initialQuery(): QueryImpl<Project> {
    return new ProjectQuery();
  }

  protected getList(params: HttpParams): Observable<Paging<Project>> {
    return this.api.project.getList(params);
  }

}

class ProjectQuery extends Project implements QueryImpl<Project> {

  constructor() {
    super();
  }

  addQuery(qb: QueryBuilder): QueryBuilder {

    qb.isDeleted(false);

    return qb;
  }

}
