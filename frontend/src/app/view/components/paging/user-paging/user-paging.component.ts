import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ApiService} from '../../../../api/api.service';
import {UserAccount} from '../../../../api/models/UserAccount';
import {debounceTime, switchMap, tap} from 'rxjs/operators';
import {QueryBuilder} from '../../../../api/query/query-builder';
import {HttpParams} from '@angular/common/http';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {Paging} from '../../../../api/dto/Paging';
import {PagingImpl, QueryImpl} from '../PagingImpl';
import {Observable} from 'rxjs/Observable';

@Component({
  selector: 'app-user-paging',
  templateUrl: './user-paging.component.html',
  styleUrls: ['./user-paging.component.css']
})
export class UserPagingComponent extends PagingImpl<UserAccount> implements OnInit {


  @Input() enableSearch: boolean = false;

  constructor(private apiService:ApiService) {
    super();
    this.pageSize = 8;
  }

  ngOnInit(): void {
  }

  search(email: string, firstName: string, lastName: string, isDeleted: boolean): void {
    this.nextSearch(new UserQuery(isDeleted, email, firstName, lastName));
  }

  protected initialQuery(): UserQuery {
    return new UserQuery(false);
  }

  protected getList(params: HttpParams): Observable<Paging<UserAccount>> {
    return this.apiService.userAccount.getList(params);
  }

}

class UserQuery extends UserAccount implements QueryImpl<UserAccount> {
  email: string;
  firstName: string;
  lastName: string;
  isDeleted: boolean;

  constructor(
    isDeleted: boolean=false,
    email: string=null,
    firstName: string=null,
    lastName: string=null)
  {
    super();

    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.isDeleted = isDeleted;
  }

  addQuery(qb: QueryBuilder): QueryBuilder {

    if(this.email) qb.like("email", this.email + "%");
    if(this.firstName) qb.like("firstName", this.firstName + "%");
    if(this.lastName) qb.like("lastName", this.lastName + "%");
    if(this.isDeleted != null) qb.isDeleted(this.isDeleted);

    return qb;
  }
}
