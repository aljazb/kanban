import {BaseEntity} from './base/BaseEntity';
import {BoardPart} from './BoardPart';
import {CardMoveType} from './enums/CardMoveType';
import {UserAccount} from './UserAccount';
import {Card} from './Card';

export class CardMove extends BaseEntity<CardMove> {

  cardMoveType: CardMoveType;
  movedBy: UserAccount;

  to: BoardPart;
  from: BoardPart;

  card: Card;

}