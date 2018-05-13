import { Component, OnInit } from '@angular/core';
import {ApiService} from '../../../api/services/api.service';
import {LoginService} from '../../../api/services/login.service';
import {Request} from '../../../api/models/Request';
import {RequestStatus} from '../../../api/models/enums/RequestStatus';
import {isNullOrUndefined} from 'util';
import {UserAccount} from '../../../api/models/UserAccount';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  rs = RequestStatus;

  user: UserAccount;

  sentRequests: Request[];
  receivedRequests: Request[];


  constructor(
    private api: ApiService,
    public loginService: LoginService) { }

  ngOnInit() {
    this.loginService.getUser().subscribe(user => {
      this.user = user;
      if(this.user != null){
        this.loadRequests()
      }
    });
  }

  loadRequests() {
    this.api.request.getUserRequests().subscribe(requests => {
      if (isNullOrUndefined(requests)) {
        this.sentRequests = this.receivedRequests = null;
        return;
      }
      this.sentRequests = requests.filter(rq => rq.sender.id == this.user.id);
      this.receivedRequests = requests.filter(rq => rq.receiver.id == this.user.id);
    })
  }

  private replaceRequest(array: Request[], request: Request) {
    if (isNullOrUndefined(request)) {
      this.loadRequests();
      return;
    }
    array[array.findIndex(arrayRequest => request.id == arrayRequest.id)] = request
  }

  acceptRequest(rq: Request) {
    this.api.request.accept(rq.id).subscribe(request => this.replaceRequest(this.receivedRequests, request));
  }

  declineRequest(rq: Request) {
    this.api.request.decline(rq.id).subscribe(request => this.replaceRequest(this.receivedRequests, request));
  }

  cancelRequest(rq: Request) {
    this.api.request.decline(rq.id).subscribe(request => this.replaceRequest(this.sentRequests, request));
  }

}
