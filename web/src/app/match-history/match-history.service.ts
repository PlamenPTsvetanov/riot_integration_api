import {inject, Injectable, signal, WritableSignal} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environment/environment';
import {MatchHistory} from './templates/match-history';

@Injectable({
  providedIn: 'root'
})
export class MatchHistoryService {


  private _fetchingLastMatches = signal(true)
  private _selectedGame = signal<string | null>(null)
  private apiServerUrl = environment.apiServerUrl;

  http = inject(HttpClient);

  public getMatchHistory(puuid: string): Observable<MatchHistory[]> {
    const params = new HttpParams()
      .set('puuid', puuid)

    return this.http.get<MatchHistory[]>(`${this.apiServerUrl}/ranked/match-history`, {params});
  }

  get fetchingLastMatches(): WritableSignal<boolean> {
    return this._fetchingLastMatches;
  }

  public setFetchingLastMatches(value: boolean) {
    this._fetchingLastMatches.set(value);
  }

  get selectedGame(): WritableSignal<string | null> {
    return this._selectedGame;
  }

  public setSelectedGame(value: string | null) {
    this._selectedGame.set(value);
  }
}
