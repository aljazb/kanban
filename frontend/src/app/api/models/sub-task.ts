import {Card} from './Card';
import {BaseEntity} from './base/BaseEntity';
import {UserAccount} from './UserAccount';

export class SubTask extends BaseEntity<SubTask> {

  name: string;
  description: string;
  workingHours: number;

  assignedTo: UserAccount;

  card: Card;

}
