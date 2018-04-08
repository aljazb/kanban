import {BaseEntity} from './base/BaseEntity';
import {RequestType} from './enums/RequestType';
import {RequestStatus} from './enums/RequestStatus';
import {UserAccount} from './UserAccount';

export class Request extends BaseEntity<Request> {
  requestType: RequestType;
  requestStatus: RequestStatus;
  referenceId: string;
  context: string;
  sender: UserAccount;
  receiver: UserAccount;
}
