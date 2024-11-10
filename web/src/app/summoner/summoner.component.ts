import {Component, Input, OnDestroy} from '@angular/core';
import {SummonerService} from './summoner.service';
import {Account} from '../account/account';
import {Summoner} from './summoner';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-summoner',
  templateUrl: './summoner.component.html',
  styleUrl: './summoner.component.css',
  standalone: true
})
export class SummonerComponent implements OnDestroy {

  @Input() account: Account;

  summoner: Summoner;
  private sub: Subscription;

  constructor(private summonerService: SummonerService) {
  }

  getSummoner() {
    this.sub = this.summonerService.getSummoner(this.account.puuid).subscribe({
      next: (summoner) => {
        console.log(summoner)
        this.summoner = summoner;
      },
      error: (error) => console.log(error)
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }
}
