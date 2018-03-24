import {Component, Input, OnInit} from '@angular/core';
import {UserAccount} from '../../../../api/models/UserAccount';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css']
})
export class UserDetailsComponent implements OnInit {

  @Input()
  user: UserAccount;

  constructor() { }

  ngOnInit() {
  }

}
