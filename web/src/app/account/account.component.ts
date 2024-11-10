import {Component, Output} from '@angular/core';
import {Account} from './account';
import {AccountService} from './account.service';
import {FormControl, FormGroup, FormGroupDirective, NgForm, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {ErrorStateMatcher} from '@angular/material/core';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrl: './account.component.css',
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, ReactiveFormsModule, MatIconModule, MatButtonModule],
})
export class AccountComponent {
  form = new FormGroup(
    {
      inGameName: new FormControl<string>('', [Validators.required]),
      tag: new FormControl<string>('', [Validators.required])
    }
  );

  matcher = new AccountErrorStateMatcher();

  @Output() account: Account;


  constructor(private accountService: AccountService) {
  }

  public getAccount(): void {
    const inGameName = this.form.value.inGameName;
    const tag = this.form.value.tag;

    console.log(inGameName, tag);
    this.accountService.getAccount(inGameName!, tag!).subscribe(
      (response: Account) => {
        this.account = response;
        console.log(response);
      }
    );
  }
}

export class AccountErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}
