import { Component, OnInit } from '@angular/core';
import {ApiService} from '../../../api/Api';
import {LoginService} from '../../../api/login.service';
import {Request} from '../../../api/models/Request';
import {RequestStatus} from '../../../api/models/enums/RequestStatus';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  sentRequests: Request[];
  receivedRequests: Request[];

  requestStatus = new RequestStatus();

  constructor(private api: ApiService, public loginService: LoginService) { }

  ngOnInit() {
    this.loadRequests()
  }

  loadRequests() {
    this.api.request.getUserRequests().subscribe(requests => {
      this.sentRequests = requests.filter(rq => rq.sender.id == this.loginService.user.id);
      this.receivedRequests = requests.filter(rq => rq.receiver.id == this.loginService.user.id);
    })
  }

  acceptRequest(rq: Request) {
    this.api.request.accept(rq.id).subscribe(() => this.loadRequests());
  }

}
