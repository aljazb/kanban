import { Component, OnInit } from '@angular/core';
import {ApiService} from '../../../../api/Api';
import {UserAccount} from '../../../../api/models/UserAccount';
import {Observable} from 'rxjs/Observable';
import {Subject} from 'rxjs/Subject';
import {debounceTime, distinctUntilChanged, switchMap} from 'rxjs/operators';
import {QueryBuilder} from '../../../../api/query/query-builder';
import {HttpParams} from '@angular/common/http';

@Component({
  selector: 'app-user-paging',
  templateUrl: './user-paging.component.html',
  styleUrls: ['./user-paging.component.scss']
})
export class UserPagingComponent implements OnInit {

  users$: Observable<UserAccount[]>;
  private searchTerms = new Subject<UserSearchTerms>();

  constructor(private apiService:ApiService) { }


  ngOnInit(): void {
    this.users$ = this.searchTerms.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap((params: UserSearchTerms) => this.apiService.userAccount.getList(params.buildQuery())),
    );

    this.search(null, null, null, false);
  }

  search(email: string, firstName: string, lastName: string, isDeleted: boolean): void {
    this.searchTerms.next(new UserSearchTerms(email, firstName, lastName, isDeleted));
  }

}

class UserSearchTerms {
  email: string;
  firstName: string;
  lastName: string;
  isDeleted: boolean;

  constructor(email: string, firstName: string, lastName: string, isDeleted: boolean) {
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.isDeleted = isDeleted;
  }

  buildQuery(): HttpParams {
    let qb = QueryBuilder.query(false);

    if(this.email){
      qb.like("email", this.email + "%")
    }

    if(this.firstName){
      qb.like("firstName", this.firstName + "%")
    }

    if(this.lastName){
      qb.like("lastName", this.lastName + "%")
    }

    if(this.isDeleted != null){
      qb.eq("isDeleted", this.isDeleted ? "true" : "false")
    }

    return qb.build();
  }
}
