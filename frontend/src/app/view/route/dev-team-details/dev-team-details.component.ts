import { Component, OnInit } from '@angular/core';
import {DevTeamFormComponent} from '../../components/forms/dev-team-form/dev-team-form.component';
import {ActivatedRoute} from '@angular/router';
import {LoginService} from '../../../api/services/login.service';
import {UserSelectionFormComponent} from '../../components/forms/user-selection-form/user-selection-form.component';
import {DevTeam} from '../../../api/models/DevTeam';
import {isNullOrUndefined} from "util";
import {UserAccount} from '../../../api/models/UserAccount';
import {ApiService} from '../../../api/services/api.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {HistoryEvent} from '../../../api/models/HistoryEvent';
import {Membership} from '../../../api/models/Membership';

@Component({
  selector: 'app-dev-team-details',
  templateUrl: './dev-team-details.component.html',
  styleUrls: ['./dev-team-details.component.css']
})
export class DevTeamDetailsComponent implements OnInit {

  user: UserAccount;

  id: string;
  devTeam: DevTeam;
  developers: UserAccount[];
  kanbanMaster: UserAccount;
  productOwner: UserAccount;
  events: HistoryEvent[];

  isAuthUserKanbanMaster: boolean;

  constructor(private route: ActivatedRoute,
              private api: ApiService,
              private loginService: LoginService,
              private modalService: NgbModal) { }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.loginService.getUser().subscribe(user => {
      this.user = user;
      this.loadData();
    });
  }

  loadData() {
    this.api.devTeam.get(this.id).subscribe(dt => {
      this.devTeam = dt;
      this.developers = DevTeam.getDevelopers(dt);
      this.kanbanMaster = DevTeam.getKanbanMaster(dt);
      this.productOwner = DevTeam.getProductOwner(dt);

      console.log(this.devTeam);
      this.isAuthUserKanbanMaster = Membership.isKanbanMaster(this.devTeam.membership);
    });

    this.api.devTeam.getEvents(this.id).subscribe(events => {
      this.events = events.sort((a: HistoryEvent, b: HistoryEvent) =>
        this.getEventDate(a).getTime() - this.getEventDate(b).getTime())
    });
  }

  sendKMInvite() {
    const modalRef = this.modalService.open(UserSelectionFormComponent);
    this.api.userAccount.getKanbanMasters().subscribe(kanbanMasters =>
      (<UserSelectionFormComponent>modalRef.componentInstance).setUsers(kanbanMasters.filter(km => km.id != this.kanbanMaster.id)));
    modalRef.result
      .then(ua => {
        if (!isNullOrUndefined(ua)) {
          this.api.request
            .createKanbanMasterInvite(this.id, ua.id, `Invite to become Kanban Master of ${this.devTeam.name}`).subscribe()
        }
      })
      .catch(reason => console.log(reason));
  }

  openDevTeamEditModal() {
    const modalRef = this.modalService.open(DevTeamFormComponent);

    (<DevTeamFormComponent> modalRef.componentInstance).initialize(this.devTeam);

    modalRef.result
      .then(value =>
        this.api.devTeam.put(value, true).subscribe(devTeam => {
          this.loadData();
        }))
      .catch(reason => console.log(reason));
  }

  getEventDate(historyEvent: HistoryEvent) {
    return HistoryEvent.getDate(historyEvent);
  }

}
