import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {PagingImpl, QueryImpl} from '../PagingImpl';
import {CardMoveRule} from '../../../../api/resource/card-move-rules';
import {CardMove} from '../../../../api/models/card-move';
import {DevTeam} from '../../../../api/models/DevTeam';
import {QueryBuilder} from '../../../../api/query/query-builder';
import {Paging} from '../../../../api/dto/Paging';
import {of} from 'rxjs/observable/of';
import {HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';

@Component({
  selector: 'app-card-move-rules-paging',
  templateUrl: './card-move-rules-paging.component.html',
  styleUrls: ['./card-move-rules-paging.component.css']
})
export class CardMoveRulesPagingComponent extends PagingImpl<CardMoveRule> implements OnInit {

  @Input()
  cardMoveRules: CardMoveRule[];

  @Output()
  onDelete: EventEmitter<CardMoveRule> = new EventEmitter();

  constructor() {
    super();
    this.pageSize = 7;
  }

  ngOnInit() {
  }

  protected initialQuery(): QueryImpl<CardMoveRule> {
    return new CardMoveQuery();
  }

  protected getList(params: HttpParams): Observable<Paging<CardMoveRule>> {
    let skip: number = Number(params.get('skip'));
    let limit: number = Number(params.get('limit'));

    if(this.cardMoveRules) {
      let items = this.cardMoveRules.slice(skip, skip + limit);
      let paging: Paging<CardMoveRule> = new Paging<CardMoveRule>(this.cardMoveRules.length, items);
      return of(paging);
    } else {
      let paging: Paging<CardMoveRule> = new Paging<CardMoveRule>(0, []);
      return of(paging);
    }
  }

  emitOnDelete(cardMoveRule: CardMoveRule) {
    this.onDelete.emit(cardMoveRule);
  }

  rolesToSting(cardMoveRule: CardMoveRule): string {

    let str = '';

    if(cardMoveRule.roleKanbanMasterAllowed) {
      str += 'Kanban master, '
    }

    if(cardMoveRule.roleProductOwnerAllowed) {
      str += 'Product owner, '
    }

    if(cardMoveRule.roleDeveloperAllowed) {
      str += 'Developer '
    }

    return str;
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
