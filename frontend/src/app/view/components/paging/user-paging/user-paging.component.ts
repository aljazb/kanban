import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {ApiService} from '../../../../api/Api';
import {UserAccount} from '../../../../api/models/UserAccount';
import {debounceTime, switchMap, tap} from 'rxjs/operators';
import {QueryBuilder} from '../../../../api/query/query-builder';
import {HttpParams} from '@angular/common/http';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {Paging} from '../../../../api/dto/Paging';

@Component({
  selector: 'app-user-paging',
  templateUrl: './user-paging.component.html',
  styleUrls: ['./user-paging.component.css']
})
export class UserPagingComponent implements OnInit {
  lastQuery: UserSearchTerms = new UserSearchTerms(false);

  @Output()
  onUserSelect: EventEmitter<any> = new EventEmitter();
  userPaging: Paging<UserAccount>;
  searchQuery = new BehaviorSubject<UserSearchTerms>(this.lastQuery);

  collectionSize: number=1;
  pageSize: number=5;
  page: number=1;

  constructor(private apiService:ApiService) { }

  ngOnInit(): void {
    this.searchQuery.pipe(
      debounceTime(300),
      switchMap((params: UserSearchTerms) => this.apiService.userAccount.getList(this.buildQuery(params))),
    ).subscribe(value => {
      this.userPaging = value;
      this.setPageCount(value.count);
    });
  }

  search(email: string, firstName: string, lastName: string, isDeleted: boolean): void {
    this.page = 1;
    this.lastQuery = new UserSearchTerms(isDeleted, email, firstName, lastName);
    this.searchQuery.next(this.lastQuery);
  }

  onUserClicked(user: UserAccount) {
    this.onUserSelect.emit(user);
  }

  refresh(): void {
    this.searchQuery.next(this.lastQuery);
  }

  setPageCount(count: number): void {
    this.collectionSize = count;
  }

  private buildQuery(search: UserSearchTerms): HttpParams {
    let qb = QueryBuilder.query(false);

    qb.limit(this.pageSize);
    qb.skip(this.pageSize * (this.page - 1));
    if(search.email) qb.like("email", search.email + "%");
    if(search.firstName) qb.like("firstName", search.firstName + "%");
    if(search.lastName) qb.like("lastName", search.lastName + "%");
    if(search.isDeleted != null) qb.eq("isDeleted", search.isDeleted ? "true" : "false");

    return qb.build();
  }

}

class UserSearchTerms {
  email: string;
  firstName: string;
  lastName: string;
  isDeleted: boolean;

  constructor(
    isDeleted: boolean=false,
    email: string=null,
    firstName: string=null,
    lastName: string=null) {

    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.isDeleted = isDeleted;
  }
}
