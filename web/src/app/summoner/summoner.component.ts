import {Component, DestroyRef, inject, Input, OnInit} from '@angular/core';
import {Account} from '../account/account';
import {SummonerService} from './summoner.service';

@Component({
  selector: 'app-summoner',
  templateUrl: './summoner.component.html',
  styleUrl: './summoner.component.css',
  standalone: true
})
export class SummonerComponent implements OnInit {
  private destroyRef = inject(DestroyRef);

  @Input() account: Account;

  summonerService = inject(SummonerService);

  ngOnInit(): void {
    const sub = this.summonerService.getSummoner(this.account.puuid).subscribe({
      next: (value) => {
        this.summonerService.summoner.set(value);
      }
    });

    this.destroyRef.onDestroy(() => {
      sub.unsubscribe();
    });
  }


}
