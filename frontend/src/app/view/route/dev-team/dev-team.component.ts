import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ApiService} from '../../../api/api.service';
import {DevTeam} from '../../../api/models/DevTeam';
import {UserAccount} from '../../../api/models/UserAccount';

@Component({
  selector: 'app-dev-team',
  templateUrl: './dev-team.component.html',
  styleUrls: ['./dev-team.component.css']
})
export class DevTeamComponent implements OnInit {

  id: string;
  devTeam: DevTeam;
  developers: UserAccount[];
  kanbanMaster: UserAccount;
  productOwner: UserAccount;

  constructor( private route: ActivatedRoute, private api: ApiService) { }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');

    this.loadData();
  }

  loadData() {
    this.api.devTeam.get(this.id).subscribe(dt => this.devTeam = dt);
    this.api.devTeam.getDevelopers(this.id).subscribe(m => this.developers = m);
    this.api.devTeam.getKanbanMaster(this.id).subscribe(km => this.kanbanMaster = km);
    this.api.devTeam.getProductOwner(this.id).subscribe(po => this.productOwner = po);
  }

}
