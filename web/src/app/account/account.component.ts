import {Component} from '@angular/core';
import {Account} from './account';
import {AccountService} from './account.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent {
  public username: string;
  public tag: string;

  public account: Account;


  constructor(private accountService: AccountService, private router: Router) {
  }

  public getAccount(username: string, tag: string): void {
    this.accountService.getAccount(username, tag).subscribe(
      (response: Account) => {
        this.account = response;
        if (this.account) {
          this.router.navigate(['/summoner']);
        }
      }
    );
  }
}
