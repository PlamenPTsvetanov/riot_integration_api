import {Injectable, Output, signal, WritableSignal} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {catchError, Observable} from 'rxjs';
import {Account} from './account';
import {environment} from '../../environment/environment';
import {Summoner} from '../summoner/summoner';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  accountFetched = signal(false);
  private _account = signal<Account | null>(null);

  private apiServerUrl = environment.apiServerUrl;

  constructor(private http: HttpClient) {
  }


  public getAccount(username: string, tag: string): Observable<Account> {
    const params = new HttpParams()
      .set('username', username)
      .set('tag', tag);

    return this.http
      .get<Account>(`${this.apiServerUrl}/accounts`, {params});
  }

  get account(): WritableSignal<Account | null> {
    return this._account;
  }
}
