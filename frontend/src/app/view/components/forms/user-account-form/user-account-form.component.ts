import { Component } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {UserAccount} from '../../../../api/models/UserAccount';

@Component({
  selector: 'app-user-account-form',
  templateUrl: './user-account-form.component.html',
  styleUrls: ['./user-account-form.component.css']
})
export class UserAccountFormComponent {

  private PASSWORD_BLANK: string = '******';

  userAccount: UserAccount = new UserAccount();

  formUserAccount: FormGroup;

  fcUsername: FormControl;
  fcEmail: FormControl;
  fcFirstName: FormControl;
  fcLastName: FormControl;

  fcPassword: FormControl;

  fcInRoleDeveloper: FormControl;
  fcInRoleKanbanMaster: FormControl;
  fcInRoleProductOwner: FormControl;
  fcInRoleAdministrator: FormControl;


  constructor(public activeModal: NgbActiveModal) {
    this.initFormControls();
    this.initFormGroup();
  }

  initFormControls(): void {
    this.fcUsername = new FormControl('', Validators.required);
    this.fcEmail = new FormControl('', [Validators.required, Validators.email]);
    this.fcFirstName = new FormControl('', Validators.required);
    this.fcLastName = new FormControl('', Validators.required);

    this.fcPassword = new FormControl('', Validators.required);

    this.fcInRoleDeveloper = new FormControl(true, Validators.required);
    this.fcInRoleKanbanMaster = new FormControl(false, Validators.required);
    this.fcInRoleProductOwner = new FormControl(false, Validators.required);
    this.fcInRoleAdministrator = new FormControl(false, Validators.required);
  }

  initFormGroup(): void {
    this.formUserAccount = new FormGroup({
      username: this.fcUsername,
      email: this.fcEmail,
      firstName: this.fcFirstName,
      lastName: this.fcLastName,

      password: this.fcPassword,

      inRoleDeveloper: this.fcInRoleDeveloper,
      inRoleKanbanMaster: this.fcInRoleKanbanMaster,
      inRoleProductOwner: this.fcInRoleProductOwner,
      inRoleAdministrator: this.fcInRoleAdministrator,
    });
  }

  setInitialProject(user: UserAccount) {
    this.userAccount = user;

    this.fcUsername.setValue(user.username);
    this.fcEmail.setValue(user.email);
    this.fcFirstName.setValue(user.firstName);
    this.fcLastName.setValue(user.lastName);

    this.fcPassword.setValue(this.PASSWORD_BLANK);

    this.fcInRoleDeveloper.setValue(user.inRoleDeveloper);
    this.fcInRoleKanbanMaster.setValue(user.inRoleKanbanMaster);
    this.fcInRoleProductOwner.setValue(user.inRoleProductOwner);
    this.fcInRoleAdministrator.setValue(user.inRoleAdministrator);
  }

  onSubmit() {
    if(this.formUserAccount.valid) {

      let ua = this.userAccount;

      ua.username = this.fcUsername.value;
      ua.email = this.fcEmail.value;
      ua.firstName = this.fcFirstName.value;
      ua.lastName = this.fcLastName.value;

      if(this.fcPassword.value != this.PASSWORD_BLANK) {
        ua.password = this.fcPassword.value;
      }

      ua.inRoleDeveloper = this.fcInRoleDeveloper.value;
      ua.inRoleKanbanMaster = this.fcInRoleKanbanMaster.value;
      ua.inRoleProductOwner = this.fcInRoleProductOwner.value;
      ua.inRoleAdministrator = this.fcInRoleAdministrator.value;

      this.activeModal.close(ua);
    }
  }

}
