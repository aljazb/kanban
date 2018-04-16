import { Component } from '@angular/core';
import {AbstractControl, FormControl, FormGroup, ValidatorFn, Validators} from '@angular/forms';
import {FormImpl} from '../form-impl';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {DTDateFormat} from '../../../../utility';

@Component({
  selector: 'app-user-account-password-form',
  templateUrl: './user-account-password-form.component.html',
  styleUrls: ['./user-account-password-form.component.css']
})
export class UserAccountPasswordFormComponent extends FormImpl {

  isSubmitted: boolean = false;

  form: FormGroup;

  fcPassword: FormControl;
  fcConfirmPassword: FormControl;


  constructor(public activeModal: NgbActiveModal) {
    super();
    this.initFormControls();
    this.initFormGroup();
  }

  initFormControls(): void {
    this.fcPassword = new FormControl('', [Validators.required, Validators.minLength(6)]);
    this.fcPassword.valueChanges.subscribe(value => this.fcConfirmPassword.patchValue(this.fcConfirmPassword.value));
    this.fcConfirmPassword = new FormControl('', this.matchesPassword(this.fcPassword));
  }

  initFormGroup(): void {
    this.form = new FormGroup({
      password: this.fcPassword,
      confirmPassword: this.fcConfirmPassword
    });
  }

  onSubmit() {
    this.isSubmitted = true;
    if(this.form.valid) {
      this.activeModal.close(this.fcPassword.value);
    } else {
      this.validateForm(this.form);
    }
  }

  matchesPassword(source: FormControl): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      let valid = source.value == control.value;
      return valid ? null : {'passwordNotMatched': {value: control.value}};
    };
  }

}
