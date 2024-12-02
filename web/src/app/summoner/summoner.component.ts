import {Component, DestroyRef, inject, Input, OnInit} from '@angular/core';
import {Account} from '../account/account';
import {SummonerService} from './summoner.service';
import {Subscription} from 'rxjs';
import {DomSanitizer} from '@angular/platform-browser';
import {SummonerRankedComponent} from '../summoner-ranked/summoner-ranked.component';
import {LastPlayedChampionsComponent} from '../last-played-champions/last-played-champions.component';

@Component({
  selector: 'app-summoner',
  templateUrl: './summoner.component.html',
  styleUrl: './summoner.component.css',
  standalone: true,
  imports: [SummonerRankedComponent, LastPlayedChampionsComponent]
})
export class SummonerComponent implements OnInit {
  private destroyRef = inject(DestroyRef);

  @Input() account: Account;
  summonerService = inject(SummonerService);
  sanitizer = inject(DomSanitizer);
  image: any;

  ngOnInit(): void {
    let iconSub: Subscription;
    const accountDataSub = this.summonerService.getSummoner(this.account!.puuid).subscribe({
      next: (value) => {
        this.summonerService._summoner.set(value);
        iconSub = this.summonerService.getIconBytes().subscribe({
          next: (blob) => {
            const objectURL = URL.createObjectURL(blob);
            console.log(objectURL);
            this.image = this.sanitizer.bypassSecurityTrustUrl(objectURL);
          }
        });
      }
    });
    this.destroyRef.onDestroy(() => {
      accountDataSub.unsubscribe();
      iconSub.unsubscribe();
    });
  }
}
