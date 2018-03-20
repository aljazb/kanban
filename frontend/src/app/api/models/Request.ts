import {BaseEntity} from './base/BaseEntity';
import {DevTeam} from './DevTeam';
import {BoardPart} from './BoardPart';
import {BoardLane} from './BoardLane';
import {RequestType} from './enums/RequestType';
import {RequestStatus} from './enums/RequestStatus';
import {UserAccountResource} from '../resource/UserAccountResource';
import {UserAccount} from './UserAccount';

export class Board extends BaseEntity<Board> {
  requestType: RequestType;
  context: string;
  referenceId: string;
  requestStatus: RequestStatus;
  sender: UserAccount;
  receiver:UserAccount;
}
