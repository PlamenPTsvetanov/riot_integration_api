import {inject, Injectable, signal, WritableSignal} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environment/environment';
import {SummonerRanked} from './summoner-ranked';

@Injectable({
  providedIn: 'root'
})
export class SummonerRankedService {
  private apiServerUrl = environment.apiServerUrl;
  _summonerRankedInfo = signal<SummonerRanked | null>(null)

  http = inject(HttpClient);

  public getSummonerRankedInfo(summonerId: string): Observable<SummonerRanked> {
    const params = new HttpParams()
      .set('summonerId', summonerId)

    return this.http.get<SummonerRanked>(`${this.apiServerUrl}/summoners/rankedInfo`, {params});
  }


  get summonerRankedInfo(): WritableSignal<SummonerRanked | null> {
    return this._summonerRankedInfo;
  }
}
