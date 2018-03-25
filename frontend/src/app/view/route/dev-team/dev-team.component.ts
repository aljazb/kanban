import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-dev-team',
  templateUrl: './dev-team.component.html',
  styleUrls: ['./dev-team.component.css']
})
export class DevTeamComponent implements OnInit {

  id: string;

  constructor( private route: ActivatedRoute) { }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
  }

}
