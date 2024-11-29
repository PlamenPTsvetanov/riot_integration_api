import {Component, inject} from '@angular/core';
import {AccountComponent} from "./account/account.component";
import {HeaderComponent} from "./header/header.component";
import {MatchHistoryComponent} from './match-history/match-history.component';
import {AccountService} from './account/account.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [HeaderComponent, AccountComponent, MatchHistoryComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

  accountService = inject(AccountService);
}
