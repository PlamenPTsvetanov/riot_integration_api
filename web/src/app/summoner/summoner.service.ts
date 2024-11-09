import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Summoner} from './summoner';
import {environment} from '../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class SummonerService {
  private apiServerUrl = environment.apiServerUrl;

  constructor(private http: HttpClient) {
  }


  public getSummoner(puuid: string): Observable<Summoner> {
    const params = new HttpParams()
      .set('puuid', puuid)

    return this.http.get<Summoner>(`${this.apiServerUrl}/summoners`, {params})
  }
}
