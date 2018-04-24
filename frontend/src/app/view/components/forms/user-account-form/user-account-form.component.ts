import { Component } from '@angular/core';
import {AbstractControl, FormControl, FormGroup, Validators} from '@angular/forms';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {UserAccount} from '../../../../api/models/UserAccount';
import {FormImpl} from '../form-impl';
import {ApiService} from '../../../../api/services/api.service';
import {AsyncValidatorFn} from '@angular/forms/src/directives/validators';
import {Observable} from 'rxjs/Observable';
import {map, switchMap, catchError} from 'rxjs/operators'
import {of} from 'rxjs/observable/of';
import {timer} from 'rxjs/observable/timer';




@Component({
  selector: 'app-user-account-form',
  templateUrl: './user-account-form.component.html',
  styleUrls: ['./user-account-form.component.css']
})
export class UserAccountFormComponent extends FormImpl {

  private DEBOUNC_TIME = 1000;

  userAccount: UserAccount = new UserAccount();

  isFormSubmitted: boolean = false;
  isFormChanged: boolean = true;

  formUserAccount: FormGroup;

  fcUsername: FormControl;
  fcEmail: FormControl;
  fcFirstName: FormControl;
  fcLastName: FormControl;

  fcInRoleDeveloper: FormControl;
  fcInRoleKanbanMaster: FormControl;
  fcInRoleProductOwner: FormControl;
  fcInRoleAdministrator: FormControl;


  constructor(public activeModal: NgbActiveModal,
              private api: ApiService) {
    super();
    this.initFormControls();
    this.initFormGroup();
  }

  initFormControls(): void {
    this.fcUsername = new FormControl('', Validators.required, this.isUsernameValid());
    this.fcEmail = new FormControl('', [Validators.required, Validators.email], this.isEmailValid());
    this.fcFirstName = new FormControl('', Validators.required);
    this.fcLastName = new FormControl('', Validators.required);

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

      inRoleDeveloper: this.fcInRoleDeveloper,
      inRoleKanbanMaster: this.fcInRoleKanbanMaster,
      inRoleProductOwner: this.fcInRoleProductOwner,
      inRoleAdministrator: this.fcInRoleAdministrator,
    });
  }

  setInitialUserName(user: UserAccount) {
    this.isFormChanged = false;
    this.formUserAccount.valueChanges.subscribe(value => {
      this.isFormChanged = this.isSetValueChanged();
    });

    this.userAccount = user;

    this.fcUsername.setValue(user.username);
    this.fcUsername.disable({ onlySelf: true});

    this.fcEmail.setValue(user.email);
    this.fcFirstName.setValue(user.firstName);
    this.fcLastName.setValue(user.lastName);

    this.fcInRoleDeveloper.setValue(user.inRoleDeveloper);
    this.fcInRoleKanbanMaster.setValue(user.inRoleKanbanMaster);
    this.fcInRoleProductOwner.setValue(user.inRoleProductOwner);
    this.fcInRoleAdministrator.setValue(user.inRoleAdministrator);
  }

  isSetValueChanged(): boolean {
    return this.fcEmail.value != this.userAccount.email ||
      this.fcFirstName.value != this.userAccount.firstName ||
      this.fcLastName.value != this.userAccount.lastName ||
      this.fcInRoleDeveloper.value != this.userAccount.inRoleDeveloper ||
      this.fcInRoleKanbanMaster.value != this.userAccount.inRoleKanbanMaster ||
      this.fcInRoleProductOwner.value != this.userAccount.inRoleProductOwner ||
      this.fcInRoleAdministrator.value != this.userAccount.inRoleAdministrator;
  }

  onSubmit() {
    this.isFormSubmitted = true;
    if(this.formUserAccount.valid) {

      let ua = this.userAccount;

      ua.username = this.fcUsername.value;
      ua.email = this.fcEmail.value;
      ua.firstName = this.fcFirstName.value;
      ua.lastName = this.fcLastName.value;

      ua.inRoleDeveloper = this.fcInRoleDeveloper.value;
      ua.inRoleKanbanMaster = this.fcInRoleKanbanMaster.value;
      ua.inRoleProductOwner = this.fcInRoleProductOwner.value;
      ua.inRoleAdministrator = this.fcInRoleAdministrator.value;

      this.activeModal.close(ua);
    } else {
      this.validateForm(this.formUserAccount);
    }
  }

  isUsernameValid(): AsyncValidatorFn {
    return (control: AbstractControl): Observable<{ [key: string]: any }> => {
      return timer(this.DEBOUNC_TIME).pipe(
        switchMap(()=> {
          let ua = new UserAccount();
          ua.id = this.userAccount.id;
          ua.username = control.value;
          return this.api.userAccount.isAvailable(ua).pipe(
            catchError(err => of(err.error)),
            map(value => {
              if(value && value.error) {
                return {'takenUsername': {value: value.error}};
              } else {
                return null;
              }
            })
          );
        }));
    };
  }

  isEmailValid(): AsyncValidatorFn {
    return (control: AbstractControl): Observable<{ [key: string]: any }> => {
      let email = control.value;
      if(this.userAccount != null && this.userAccount.email != email) {
        return timer(this.DEBOUNC_TIME).pipe(
          switchMap(()=> {
            let ua = new UserAccount();
            ua.id = this.userAccount.id;
            ua.email = email;
            return this.api.userAccount.isAvailable(ua).pipe(
              catchError(err => of(err.error)),
              map(value => {
                if(value && value.error) {
                  return {'takenEmail': {value: value.error}};
                } else {
                  return null;
                }
              })
            );
          }));
      } else {
        return of(null);
      }
    };
  }

}
