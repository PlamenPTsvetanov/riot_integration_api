import {Component, DestroyRef, inject, Input, OnInit, Output} from '@angular/core';
import {LastPlayedChampionService} from './last-played-champion.service';
import {LastPlayedChampion} from './last-played-champion';
import {MatProgressSpinner} from '@angular/material/progress-spinner';

@Component({
  selector: 'app-last-played-champions',
  standalone: true,
  imports: [
    MatProgressSpinner
  ],
  templateUrl: './last-played-champions.component.html',
  styleUrl: './last-played-champions.component.css'
})

export class LastPlayedChampionsComponent implements OnInit {
  private destroyRef = inject(DestroyRef);
  @Input() puuid: string;
  @Output() lastPlayedChampions: Array<LastPlayedChampion> = [];

  lpcService = inject(LastPlayedChampionService);

  ngOnInit(): void {
    this.lpcService.setIsFetching(true)
    const sub = this.lpcService.getLastChampionsData(this.puuid)
      .subscribe({
          next: (champs) => {
            this.lastPlayedChampions = champs;
          },
          complete: () => {
            this.lpcService.setIsFetching(false)
          }
        }
      )
    this.destroyRef.onDestroy(() => {
      sub.unsubscribe();
    });
  }

}
