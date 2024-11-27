import {Component, inject, Input, OnInit, Output, signal} from '@angular/core';
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
  @Input() puuid: string;
  @Output() lastPlayedChampions: Array<LastPlayedChampion> = [];
  isFetching = signal(false);
  lpcService = inject(LastPlayedChampionService);

  ngOnInit(): void {
    this.isFetching.set(true)
    this.lpcService.getLastChampionsData(this.puuid)
      .subscribe({
          next: (champs) => {
            this.lastPlayedChampions = champs;
          },
          complete: () => {
            this.isFetching.set(false);
          }
        }
      )
  }

}
