import { Component, OnInit } from '@angular/core';
import {DevTeam} from '../../../api/models/DevTeam';
import {Router} from '@angular/router';
import {DevTeamFormComponent} from '../../components/forms/dev-team-form/dev-team-form.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ApiService} from '../../../api/api.service';
import {LoginService} from '../../../api/login.service';
import {Membership} from '../../../api/models/Membership';
import {MemberType} from '../../../api/models/enums/MemberType';

@Component({
  selector: 'app-dev-team',
  templateUrl: './dev-team.component.html',
  styleUrls: ['./dev-team.component.css']
})
export class DevTeamComponent implements OnInit {

  isKanbanMaster: boolean;

  constructor(private router: Router,
              private apiService:ApiService,
              private loginService: LoginService,
              private modalService: NgbModal) { }

  ngOnInit() {
    this.loginService.getUser().subscribe(user => {
      this.isKanbanMaster = user.inRoleKanbanMaster
    });
  }

  goToDevTeam(devTeam: DevTeam): void {
    this.router.navigate(['/dev-team/' + devTeam.id]);
  }

  openDevTeamCreateModal() {
    this.loginService.getUser().subscribe(value => {
      let dt = new DevTeam();
      dt.joinedUsers = [];
      dt.joinedUsers.push(new Membership(MemberType.KANBAN_MASTER, value));

      const modalRef = this.modalService.open(DevTeamFormComponent);
      (<DevTeamFormComponent> modalRef.componentInstance).initialize(dt);

      modalRef.result.then(value =>
        this.apiService.devTeam.post(value, true).subscribe(devTeam => {
          this.router.navigate([`/dev-team/${devTeam.id}`]);
        })
      ).catch(reason => console.log(reason));
    });
  }

}
