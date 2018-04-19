import {FormControl, FormGroup} from '@angular/forms';

export class FormImpl {

  constructor() { }

  protected validateForm(form: FormGroup): void {
    Object.keys(form.controls).forEach(field => {
      const control = form.get(field);
      if (control instanceof FormControl) {
        control.markAsTouched();
        control.patchValue(control.value);
      }
    });
  }

}
