import { Component } from '@angular/core';
import {UserAccount} from '../../../../api/models/UserAccount';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ApiService} from '../../../../api/api.service';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {DevTeam} from '../../../../api/models/DevTeam';
import {isNullOrUndefined} from 'util';
import {UserAccountMTMDevTeam} from '../../../../api/models/mtm/UserAccountMTMDevTeam';
import {MemberType} from '../../../../api/models/enums/MemberType';
import {LoginService} from '../../../../api/login.service';

@Component({
  selector: 'app-dev-team-form',
  templateUrl: './dev-team-form.component.html',
  styleUrls: ['./dev-team-form.component.css']
})
export class DevTeamFormComponent {

  devTeam = new DevTeam();
  formDevTeam: FormGroup;
  kanbanMaster: UserAccount;

  allDevelopers: UserAccount[];

  productOwners: UserAccount[];
  availableDevelopers: UserAccount[];

  fcName: FormControl;
  fcProductOwner: FormControl;
  fcSelectDeveloper: FormControl;
  selectedDevelopers: UserAccount[];

  emptyFields = false;

  constructor(public activeModal: NgbActiveModal,
              private api: ApiService,
              private loginService: LoginService) {
    this.loadUsers();
    this.initFormControls();
    this.initFormGroup();
    this.selectedDevelopers = [];
  }

  loadUsers() {
    this.api.userAccount.getProductOwners().subscribe(productOwners => this.productOwners = productOwners);
    this.api.userAccount.getDevelopers().subscribe(developers => {
      this.allDevelopers = developers;
      this.availableDevelopers = developers.map(x => Object.assign({}, x)); // copy

      if (!isNullOrUndefined(this.devTeam)) {
        this.fcName.setValue(this.devTeam.name);
        this.fcProductOwner.setValue(DevTeam.getProductOwner(this.devTeam).id);

        this.availableDevelopers = this.allDevelopers.filter(dev => !DevTeam.getDevelopersIds(this.devTeam).includes(dev.id));
        this.selectedDevelopers = this.allDevelopers.filter( dev => DevTeam.getDevelopersIds(this.devTeam).includes(dev.id));
      }
    });
    this.loginService.getUser().subscribe(u => this.kanbanMaster = u);
  }

  initFormControls(): void {
    this.fcName = new FormControl('', Validators.required);
    this.fcProductOwner = new FormControl(null, Validators.required);
    this.fcSelectDeveloper = new FormControl(null);

    this.fcSelectDeveloper.valueChanges.subscribe(value => {
      if (!isNullOrUndefined(value)) {
        this.addDeveloper(value);
        this.fcSelectDeveloper.setValue(null);
      }
    })
  }

  initFormGroup(): void {
    this.formDevTeam = new FormGroup({
      name: this.fcName,
      productOwner: this.fcProductOwner,
      selectDeveloper: this.fcSelectDeveloper,
    });
  }

  setInitialDevTeam(devTeam: DevTeam) {
    this.devTeam = devTeam;
  }

  onSubmit() {
    if (this.formDevTeam.valid) {
      this.emptyFields = false;

      let d = this.devTeam;
      d.name = this.fcName.value;

      d.joinedUsers = [];
      let po = this.productOwners.filter(e => e.id == this.fcProductOwner.value)[0];
      let po_mtm = new UserAccountMTMDevTeam();
      po_mtm.memberType = MemberType.PRODUCT_OWNER;
      po_mtm.userAccount = po;
      d.joinedUsers.push(po_mtm);

      let km_mtm = new UserAccountMTMDevTeam();
      km_mtm.memberType = MemberType.KANBAN_MASTER;
      km_mtm.userAccount = this.kanbanMaster;
      d.joinedUsers.push(km_mtm);

      this.selectedDevelopers.forEach(dev => {
        let dev_mtm = new UserAccountMTMDevTeam();

        if (this.kanbanMaster.id == dev.id) {
          km_mtm.memberType = MemberType.DEVELOPER_AND_KANBAN_MASTER;
          return;
        } else if (po.id == dev.id) {
          po_mtm.memberType = MemberType.DEVELOPER_AND_PRODUCT_OWNER;
          return;
        } else {
          dev_mtm.memberType = MemberType.DEVELOPER;
        }

        dev_mtm.userAccount = dev;
        d.joinedUsers.push(dev_mtm);
      });

      console.log(d); // TODO rem
      this.activeModal.close(d);
    } else {
      this.emptyFields = true;
    }
  }

  private addDeveloper(developerId: string) {
    this.selectedDevelopers.push(this.availableDevelopers.find(e => e.id == developerId));

    let idx = this.availableDevelopers.findIndex(e => e.id == developerId);
    this.availableDevelopers.splice(idx, 1)
  }

  removeDeveloper(developerId: string) {
    let idx = this.selectedDevelopers.findIndex(e => e.id == developerId);
    this.selectedDevelopers.splice(idx, 1);

    this.availableDevelopers.push(this.allDevelopers.find(e => e.id == developerId))
  }
}
