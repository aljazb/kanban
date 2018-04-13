import { Component } from '@angular/core';
import {UserAccount} from '../../../../api/models/UserAccount';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ApiService} from '../../../../api/api.service';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {DevTeam} from '../../../../api/models/DevTeam';
import {isNullOrUndefined} from 'util';
import {Membership} from '../../../../api/models/Membership';
import {MemberType} from '../../../../api/models/enums/MemberType';
import {LoginService} from '../../../../api/login.service';

@Component({
  selector: 'app-dev-team-form',
  templateUrl: './dev-team-form.component.html',
  styleUrls: ['./dev-team-form.component.css']
})
export class DevTeamFormComponent {

  devTeam;

  formDevTeam: FormGroup;
  fcName: FormControl;
  fcProductOwner: FormControl;
  fcSelectDeveloper: FormControl;

  kanbanMaster: UserAccount;
  previousSelectedProductOwner: UserAccount;

  allDevelopers: Map<string, UserAccount>;
  productOwners: Array<UserAccount>;
  availableDevelopers: Array<UserAccount>;
  selectedDevelopers: Array<UserAccount>;

  emptyFields = false;

  constructor(public activeModal: NgbActiveModal,
              private api: ApiService,
              private loginService: LoginService)
  {
    this.initFormControls();
    this.initFormGroup();
  }

  initFormControls(): void {
    this.fcName = new FormControl('', Validators.required);

    this.fcProductOwner = new FormControl(null, Validators.required);
    this.fcProductOwner.valueChanges.subscribe(id => this.selectProductOwner(id));

    this.fcSelectDeveloper = new FormControl(null);
    this.fcSelectDeveloper.valueChanges.subscribe(value => {
      if (!isNullOrUndefined(value)) {
        this.addDeveloper(value);
        this.fcSelectDeveloper.setValue(null);
      }
    });
  }

  initFormGroup(): void {
    this.formDevTeam = new FormGroup({
      name: this.fcName,
      productOwner: this.fcProductOwner,
      selectDeveloper: this.fcSelectDeveloper,
    });
  }

  private buildMap(users: UserAccount[]): Map<string, UserAccount> {
    let map: Map<string, UserAccount> = new Map<string, UserAccount>();
    users.forEach(user => map.set(user.id, user));
    return map;
  }

  selectProductOwner(productOwnerId: string): void {
    let productOwner = this.productOwners.find(user => user.id == productOwnerId);
    if(productOwner.inRoleDeveloper) {
      this.deleteUser(this.availableDevelopers, productOwnerId);
    }
    if(this.previousSelectedProductOwner && this.previousSelectedProductOwner.inRoleDeveloper) {
      this.availableDevelopers.push(this.previousSelectedProductOwner);
    }
    this.previousSelectedProductOwner = productOwner;
  }

  addDeveloper(developerId: string) {
    let developer = this.allDevelopers.get(developerId);

    if(developer.inRoleProductOwner) {
      this.deleteUser(this.productOwners, developerId);
    }

    this.selectedDevelopers.push(developer);
    this.deleteUser(this.availableDevelopers, developerId);
  }

  removeDeveloper(developerId: string) {
    let developer = this.allDevelopers.get(developerId);

    if(developer.inRoleProductOwner){
      this.productOwners.push(developer);
    }

    this.deleteUser(this.selectedDevelopers, developerId);
    this.availableDevelopers.push(developer)
  }

  initialize(devTeam: DevTeam) {
    this.devTeam = devTeam;

    this.fcName.setValue(this.devTeam.name);

    this.kanbanMaster = DevTeam.getKanbanMaster(this.devTeam);
    this.previousSelectedProductOwner = DevTeam.getProductOwner(this.devTeam);

    let devIds = DevTeam.getDevelopersIds(this.devTeam);

    this.api.userAccount.getDevelopers().subscribe(developers => {
      this.allDevelopers = this.buildMap(developers);
      this.availableDevelopers = Array.from(developers);

      this.selectedDevelopers = this.availableDevelopers.filter( dev => devIds.includes(dev.id));
      this.availableDevelopers = this.availableDevelopers.filter(dev => !devIds.includes(dev.id));

      this.api.userAccount.getProductOwners().subscribe(productOwners => {
        this.productOwners = productOwners.filter(productOwner => !devIds.includes(productOwner.id));
        if(this.kanbanMaster.inRoleProductOwner) {
          this.deleteUser(this.productOwners, this.kanbanMaster.id);
        }
        if(this.previousSelectedProductOwner != null) {
          this.fcProductOwner.setValue(this.previousSelectedProductOwner.id);
        }
        if(this.previousSelectedProductOwner != null && this.previousSelectedProductOwner.inRoleDeveloper) {
          this.deleteUser(this.availableDevelopers, this.previousSelectedProductOwner.id);
        }
      });
    });
  }

  private deleteUser(users: UserAccount[], userId: string) {
    let index = users.findIndex(user => user.id == userId);
    if(index >= 0) {
      users.splice(index,  1);
    }
  }

  onSubmit() {
    if (this.formDevTeam.valid && this.selectedDevelopers.length > 0) {
      let dt = this.devTeam;
      dt.name = this.fcName.value;

      dt.joinedUsers = [];

      let po = this.productOwners.filter(e => e.id == this.fcProductOwner.value)[0];
      let po_mtm = new Membership(MemberType.PRODUCT_OWNER, po);
      dt.joinedUsers.push(po_mtm);

      let km_mtm = new Membership(MemberType.KANBAN_MASTER, this.kanbanMaster);
      dt.joinedUsers.push(km_mtm);

      this.selectedDevelopers.forEach(dev => {
        if (this.kanbanMaster.id == dev.id) {
          km_mtm.memberType = MemberType.DEVELOPER_AND_KANBAN_MASTER;
        } else if (po.id == dev.id) {
          po_mtm.memberType = MemberType.DEVELOPER_AND_PRODUCT_OWNER;
        } else {
          dt.joinedUsers.push(new Membership(MemberType.DEVELOPER, dev));
        }
      });

      this.activeModal.close(dt);
    } else {
      this.emptyFields = true;
    }
  }

}
