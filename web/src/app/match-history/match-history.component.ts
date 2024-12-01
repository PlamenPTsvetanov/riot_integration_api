import {Component, DestroyRef, inject, Input, OnInit, Output} from '@angular/core';
import {MatchHistoryService} from './match-history.service';
import {MatchHistory} from './templates/match-history';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-match-history',
  standalone: true,
  imports: [
    NgClass
  ],
  templateUrl: './match-history.component.html',
  styleUrl: './match-history.component.css'
})
export class MatchHistoryComponent implements OnInit {
  private destroyRef = inject(DestroyRef);

  mhService = inject(MatchHistoryService)
  @Input() puuid: string
  @Output() matchHistory: Array<MatchHistory> = []


  ngOnInit(): void {
    this.mhService.setFetchingLastMatches(false)
    this.getMatchHistory();
  }

  public getMatchHistory() {
    this.mhService.setFetchingLastMatches(true)
    const sub = this.mhService.getMatchHistory(this.puuid).subscribe({
      next: gameInfo => {
        this.matchHistory.push.apply(this.matchHistory, gameInfo)
      },
      complete: () => {
        console.log(this.matchHistory);

        this.mhService.setFetchingLastMatches(false)
      }
    });

    this.destroyRef.onDestroy(() => {
      sub.unsubscribe();
    });
  }

  selectGame(gameUuid: string | null) {
    if (this.mhService.selectedGame() === gameUuid) {
      this.mhService.setSelectedGame(null);
    } else {
      this.mhService.setSelectedGame(gameUuid);
    }
  }

  protected readonly Math = Math;
}
