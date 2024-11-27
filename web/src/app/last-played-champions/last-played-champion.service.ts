import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../environment/environment';
import {LastPlayedChampion} from './last-played-champion';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LastPlayedChampionService {
  private apiServerUrl = environment.apiServerUrl;
  http = inject(HttpClient);

  public getLastChampionsData(puuid: string): Observable<LastPlayedChampion[]> {
    const params = new HttpParams()
      .set('puuid', puuid);

    return this.http.get<LastPlayedChampion[]>(`${this.apiServerUrl}/ranked/champion-stats`, {
      params
    });
  }
}
