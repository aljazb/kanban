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

  devTeam = new DevTeam();

  formDevTeam: FormGroup;
  fcName: FormControl;
  fcProductOwner: FormControl;
  fcSelectDeveloper: FormControl;

  kanbanMaster: UserAccount;
  previousSelectedProductOwner: UserAccount;
  productOwners: UserAccount[];
  availableDevelopers: UserAccount[];

  allDevelopers: UserAccount[];
  selectedDevelopers: UserAccount[];

  emptyFields = false;

  constructor(public activeModal: NgbActiveModal,
              private api: ApiService,
              private loginService: LoginService)
  {
    this.selectedDevelopers = [];
    this.initFormControls();
    this.initFormGroup();
    this.loadUsers();
  }

  initFormControls(): void {
    this.fcName = new FormControl('', Validators.required);

    this.fcProductOwner = new FormControl(null, Validators.required);
    this.fcProductOwner.valueChanges.subscribe(id => this.selectProductOwner(this.productOwners.find(user => user.id == id)));

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

  loadUsers() {
    this.api.userAccount.getProductOwners().subscribe(productOwners => this.productOwners = productOwners);
    this.api.userAccount.getDevelopers().subscribe(developers => {
      this.allDevelopers = developers;
      this.availableDevelopers = developers.map(x => Object.assign({}, x)); // copy
    });
    this.loginService.getUser().subscribe(u => this.kanbanMaster = u);
  }

  selectProductOwner(productOwner: UserAccount): void {
    console.log(productOwner);
    if(productOwner.inRoleDeveloper) {
      this.availableDevelopers = this.availableDevelopers.filter(value => value.id != productOwner.id);
    }
    if(this.previousSelectedProductOwner && this.previousSelectedProductOwner.inRoleDeveloper) {
      this.availableDevelopers.push(this.previousSelectedProductOwner)
    }
    this.previousSelectedProductOwner = productOwner;
  }

  addDeveloper(developerId: string) {
    let developer: UserAccount = this.availableDevelopers.find(e => e.id == developerId);

    if(developer.inRoleProductOwner) {
      this.productOwners = this.productOwners.filter(productOwner => productOwner.id != developer.id);
    }

    this.selectedDevelopers.push(developer);

    let idx = this.availableDevelopers.findIndex(e => e.id == developerId);
    this.availableDevelopers.splice(idx, 1)
  }

  removeDeveloper(developerId: string) {
    let developer = this.selectedDevelopers.find(developer => developer.id == developerId);
    if(developer.inRoleProductOwner){
      this.productOwners.push(developer);
    }

    let idx = this.selectedDevelopers.findIndex(e => e.id == developerId);
    this.selectedDevelopers.splice(idx, 1);


    this.availableDevelopers.push(this.allDevelopers.find(e => e.id == developerId))
  }

  setInitialDevTeam(devTeam: DevTeam) {
    this.devTeam = devTeam;
    this.previousSelectedProductOwner = DevTeam.getProductOwner(this.devTeam);


    let devIds = DevTeam.getDevelopersIds(this.devTeam);
    this.availableDevelopers = this.allDevelopers.filter(dev => !devIds.includes(dev.id));
    this.selectedDevelopers = this.allDevelopers.filter( dev => devIds.includes(dev.id));

    if(this.previousSelectedProductOwner != null && this.previousSelectedProductOwner.inRoleDeveloper) {
      this.availableDevelopers = this.availableDevelopers.filter( dev => dev.id != this.previousSelectedProductOwner.id);
    }

    this.fcName.setValue(this.devTeam.name);
    if(this.previousSelectedProductOwner != null){
      this.fcProductOwner.setValue(this.previousSelectedProductOwner.id);
    }
  }

  onSubmit() {
    if (this.formDevTeam.valid) {
      this.emptyFields = false;

      let d = this.devTeam;
      d.name = this.fcName.value;

      d.joinedUsers = [];
      let po = this.productOwners.filter(e => e.id == this.fcProductOwner.value)[0];
      let po_mtm = new Membership();
      po_mtm.memberType = MemberType.PRODUCT_OWNER;
      po_mtm.userAccount = po;
      d.joinedUsers.push(po_mtm);

      let km_mtm = new Membership();
      km_mtm.memberType = MemberType.KANBAN_MASTER;
      km_mtm.userAccount = this.kanbanMaster;
      d.joinedUsers.push(km_mtm);

      this.selectedDevelopers.forEach(dev => {
        let dev_mtm = new Membership();

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

      this.activeModal.close(d);
    } else {
      this.emptyFields = true;
    }
  }

}
