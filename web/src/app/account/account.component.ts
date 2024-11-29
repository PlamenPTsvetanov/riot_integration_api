import {Component, DestroyRef, inject} from '@angular/core';
import {Account} from './account';
import {AccountService} from './account.service';
import {FormControl, FormGroup, FormGroupDirective, NgForm, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {ErrorStateMatcher} from '@angular/material/core';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {SummonerComponent} from '../summoner/summoner.component';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrl: './account.component.css',
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, ReactiveFormsModule, MatIconModule, MatButtonModule, SummonerComponent],
})
export class AccountComponent {

  private destroyRef = inject(DestroyRef);

  form = new FormGroup(
    {
      inGameName: new FormControl<string>('', [Validators.required]),
      tag: new FormControl<string>('', [Validators.required])
    }
  );
  matcher = new AccountErrorStateMatcher();

  accountService = inject(AccountService);

  public getAccount(): void {
    this.accountService.account.set(null);
    const inGameName = this.form.value.inGameName;
    const tag = this.form.value.tag;
    const sub = this.accountService.getAccount(inGameName!, tag!)
      .subscribe({
          next: (response: Account) => {
            this.accountService.account.set(response);
          },
          complete: () => {
            this.accountService.accountFetched.set(true);
          }
        }
      );
    this.destroyRef.onDestroy(() => {
      sub.unsubscribe();
    });
  }

  public getAccountFetched() {
    return this.accountService.accountFetched();
  }
}

export class AccountErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}
