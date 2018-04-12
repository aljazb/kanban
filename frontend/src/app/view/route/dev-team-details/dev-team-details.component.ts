import { Component, OnInit } from '@angular/core';
import {DevTeamFormComponent} from '../../components/forms/dev-team-form/dev-team-form.component';
import {ActivatedRoute} from '@angular/router';
import {LoginService} from '../../../api/login.service';
import {UserSelectionFormComponent} from '../../components/forms/user-selection-form/user-selection-form.component';
import {DevTeam} from '../../../api/models/DevTeam';
import {isNullOrUndefined} from "util";
import {UserAccount} from '../../../api/models/UserAccount';
import {ApiService} from '../../../api/api.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

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

  isUserKanbanMaster: boolean;

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

      this.isUserKanbanMaster = this.user.id === this.kanbanMaster.id;
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

    (<DevTeamFormComponent> modalRef.componentInstance).setInitialDevTeam(this.devTeam);

    modalRef.result
      .then(value =>
        this.api.devTeam.put(value, true).subscribe(devTeam => {
          this.loadData();
        }))
      .catch(reason => console.log(reason));
  }

}
