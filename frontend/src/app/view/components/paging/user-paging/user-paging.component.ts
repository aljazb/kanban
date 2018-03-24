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
  styleUrls: ['./user-paging.component.scss']
})
export class UserPagingComponent implements OnInit {
  lastQuery: UserSearchTerms = new UserSearchTerms(false);

  @Output()
  onUserSelect: EventEmitter<any> = new EventEmitter();
  userPaging: Paging<UserAccount>;
  searchQuery = new BehaviorSubject<UserSearchTerms>(this.lastQuery);

  pageIndex: number=1;
  pageSize: number=5;
  pageCount: number;
  pageLabels: number[];


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
    this.pageIndex = 1;
    this.lastQuery = new UserSearchTerms(isDeleted, email, firstName, lastName);
    this.searchQuery.next(this.lastQuery);
  }

  onUserClicked(user: UserAccount) {
    this.onUserSelect.emit(user);
  }

  refresh(): void {
    this.searchQuery.next(this.lastQuery);
  }

  nextPage(): void {
    this.setPageIndex(this.pageIndex + 1);
  }

  previousPage(): void {
    this.setPageIndex(this.pageIndex - 1);
  }

  firstPage(): void {
    this.setPageIndex(1);
  }

  lastPage(): void {
    this.setPageIndex(this.pageCount);
  }

  setPageIndex(index: number): void {
    if(index < 1){
      index = 1;
    } else if(index >= this.pageCount){
      index = this.pageCount
    }
    if(this.pageIndex != index) {
      this.pageIndex = index;
      this.searchQuery.next(this.lastQuery);
    }
  }

  setPageCount(count: number): void {
    this.pageCount = Math.ceil(count / this.pageSize);
    this.generateLabels();
  }

  private generateLabels(): void {
    let offset = 3 - this.pageIndex;
    if(offset < 0) {
      offset = this.pageCount - 2 - this.pageIndex;
      if(offset > 0){
        offset = 0;
      }
    }

    let numberOfLabels = this.pageCount < 5 ? this.pageCount : 5;
    let labels: number[] = [];
    for(let i=0; i<numberOfLabels; i++) {
      labels.push(this.pageIndex - 2 + i + offset);
    }

    this.pageLabels = labels;
  }

  private buildQuery(search: UserSearchTerms): HttpParams {
    let qb = QueryBuilder.query(false);

    qb.limit(this.pageSize);
    qb.skip(this.pageSize * (this.pageIndex - 1));
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
