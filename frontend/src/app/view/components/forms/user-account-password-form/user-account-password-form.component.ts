import { Component } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {FormImpl} from '../form-impl';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-user-account-password-form',
  templateUrl: './user-account-password-form.component.html',
  styleUrls: ['./user-account-password-form.component.css']
})
export class UserAccountPasswordFormComponent extends FormImpl {

  isSubmitted: boolean = false;
  isConfirmPasswordValid: boolean = false;

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
    this.fcConfirmPassword = new FormControl('');

    this.fcConfirmPassword.valueChanges.subscribe(value => {
      this.isConfirmPasswordValid = this.fcPassword.value === this.fcConfirmPassword.value;
    });
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
    }
  }

}
