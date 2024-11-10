import {inject, Injectable, signal, WritableSignal} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Summoner} from './summoner';
import {environment} from '../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class SummonerService {
  private apiServerUrl = environment.apiServerUrl;
  _summoner = signal<Summoner | null>(null)

  http = inject(HttpClient);

  public getSummoner(puuid: string): Observable<Summoner> {
    const params = new HttpParams()
      .set('puuid', puuid)

    return this.http.get<Summoner>(`${this.apiServerUrl}/summoners`, {params});
  }

  public getIconBytes(): Observable<Blob> {
    const params = new HttpParams()
      .set('iconId', this._summoner()!.profileIconId);

    return this.http.get<Blob>(`${this.apiServerUrl}/summoners/icon`, {
      params,
      responseType: "blob" as "json"
    });
  }

  get summoner(): WritableSignal<Summoner | null> {
    return this._summoner;
  }
}
