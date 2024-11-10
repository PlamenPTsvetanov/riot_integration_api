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
  private _summoner = signal<Summoner | null>(null)

  http = inject(HttpClient);


  public getSummoner(puuid: string): Observable<Summoner> {
    console.log(puuid);
    const params = new HttpParams()
      .set('puuid', puuid)

    return this.http.get<Summoner>(`${this.apiServerUrl}/summoners`, {params})
  }

  get summoner(): WritableSignal<Summoner | null> {
    return this._summoner;
  }
}
