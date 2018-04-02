import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ApiService} from '../../../api/api.service';
import {DevTeam} from '../../../api/models/DevTeam';
import {UserAccount} from '../../../api/models/UserAccount';
import {LoginService} from '../../../api/login.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {UserSelectionFormComponent} from '../../components/forms/user-selection-form/user-selection-form.component';
import {DevTeamFormComponent} from '../../components/forms/dev-team-form/dev-team-form.component';
import {isNullOrUndefined} from 'util';

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

  constructor( private route: ActivatedRoute,
               private api: ApiService,
               private loginService: LoginService,
               private modalService: NgbModal,
               private router: Router) { }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');

    this.loadData();
  }

  loadData() {
    this.api.devTeam.get(this.id).subscribe(dt => {
      this.devTeam = dt;
      this.developers = DevTeam.getDevelopers(dt);
      this.kanbanMaster = DevTeam.getKanbanMaster(dt);
      this.productOwner = DevTeam.getProductOwner(dt);
      this.loginService.getUser().subscribe(user => this.isUserKanbanMaster = user.id == this.kanbanMaster.id)
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

    (<DevTeamFormComponent>modalRef.componentInstance).setInitialDevTeam(this.devTeam);

    modalRef.result
      .then(value =>
        this.api.devTeam.put(value, true).subscribe(devTeam => {
          this.loadData();
        }))
      .catch(reason => console.log(reason));
  }
}
