import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ApiService} from '../../../api/api.service';
import {DevTeam} from '../../../api/models/DevTeam';
import {UserAccount} from '../../../api/models/UserAccount';
import {LoginService} from '../../../api/login.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {UserSelectionFormComponent} from '../../components/forms/user-selection-form/user-selection-form.component';

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

  isUserKanbanMaster;

  constructor( private route: ActivatedRoute, private api: ApiService, private loginService: LoginService, private modalService: NgbModal) { }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');

    this.loadData();
  }

  loadData() {
    this.api.devTeam.get(this.id).subscribe(dt => this.devTeam = dt);
    this.api.devTeam.getDevelopers(this.id).subscribe(m => this.developers = m);
    this.api.devTeam.getKanbanMaster(this.id).subscribe(km => {
      this.kanbanMaster = km;
      this.loginService.getUser().subscribe(user => this.isUserKanbanMaster = user.id == this.kanbanMaster.id)
    });
    this.api.devTeam.getProductOwner(this.id).subscribe(po => this.productOwner = po);
  }

  kick(user: UserAccount) {
    this.api.devTeam.kickMember(this.id, user.id).subscribe(() => this.loadData());
  }

  demotePO(po: UserAccount) {
    this.api.devTeam.demotePO(this.id, po.id).subscribe(() => this.loadData());
  }

  sendDevInvite() {
    const modalRef = this.modalService.open(UserSelectionFormComponent);
    (<UserSelectionFormComponent>modalRef.componentInstance).filterUsersWith(user => !this.developers.find(u => u.id == user.id));
    modalRef.result
      .then(ua => this.api.request
        .createDevTeamInvite(this.id, ua.id, `Invite to team ${this.devTeam.name}`).subscribe())
      .catch(reason => console.log(reason));
  }

  sendKMInvite() {
    const modalRef = this.modalService.open(UserSelectionFormComponent);
    (<UserSelectionFormComponent>modalRef.componentInstance).filterUsersWith(user => (this.kanbanMaster) ? user.id != this.kanbanMaster.id : true);
    modalRef.result
      .then(ua => this.api.request
        .createKanbanMasterInvite(this.id, ua.id, `Invite to become Kanban Master of ${this.devTeam.name}`).subscribe())
      .catch(reason => console.log(reason));
  }

  sendPOInvite() {
    const modalRef = this.modalService.open(UserSelectionFormComponent);
    (<UserSelectionFormComponent>modalRef.componentInstance).filterUsersWith(user => (this.productOwner) ? user.id != this.productOwner.id : true);
    modalRef.result
      .then(ua => this.api.request
        .createProductOwnerInvite(this.id, ua.id, `Invite to become Product Owner of ${this.devTeam.name}`).subscribe())
      .catch(reason => console.log(reason));
  }
}
