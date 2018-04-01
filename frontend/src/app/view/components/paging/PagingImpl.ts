import {EventEmitter, Output} from '@angular/core';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {UserAccount} from '../../../api/models/UserAccount';
import {Paging} from '../../../api/dto/Paging';
import {BaseEntity} from '../../../api/models/base/BaseEntity';
import {QueryBuilder} from '../../../api/query/query-builder';
import {HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {debounceTime, switchMap} from 'rxjs/operators';

export abstract class PagingImpl<E extends BaseEntity<E>> {

  @Output() onSelect: EventEmitter<any> = new EventEmitter();

  lastQuery: QueryImpl<E> = this.initialQuery();
  paging: Paging<E>;
  searchQuery = new BehaviorSubject<QueryImpl<E>>(this.lastQuery);

  collectionSize: number=1;
  pageSize: number=5;
  page: number=1;

  protected abstract initialQuery(): QueryImpl<E>
  protected abstract getList(params: HttpParams): Observable<Paging<E>>;


  constructor() {
    this.init();
  }

  init(): void {
    this.searchQuery.pipe(
      debounceTime(300),
      switchMap((params: QueryImpl<E>) => this.getList(this.buildQuery(params))),
    ).subscribe(value => this.setPaging(value));
  }

  itemSelected(user: UserAccount) {
    this.onSelect.emit(user);
  }

  refresh(): void {
    this.nextSearch(this.lastQuery);
  }

  protected setPaging(paging: Paging<E>): void {
    this.paging = paging;
    this.collectionSize = paging.count;
  }

  protected nextSearch(query: QueryImpl<E>){
    this.page = 1;
    this.lastQuery = query;
    this.searchQuery.next(this.lastQuery);
  }

  protected buildQuery(search: QueryImpl<E>): HttpParams {
    let qb = QueryBuilder.query(false);

    qb.limit(this.pageSize);
    qb.skip(this.pageSize * (this.page - 1));

    search.addQuery(qb);

    return qb.build();
  }

}

export interface QueryImpl<T> {

  addQuery(qb: QueryBuilder): QueryBuilder;

}
