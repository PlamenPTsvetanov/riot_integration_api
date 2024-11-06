import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Account} from './account';
import {environment} from '../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private apiServerUrl = environment.apiServerUrl;

  constructor(private http: HttpClient) {
  }


  public getAccount(username: string, tag: string): Observable<Account> {
    const params = new HttpParams()
      .set('username', username)
      .set('tag', tag);

    return this.http.get<Account>(`${this.apiServerUrl}/accounts/get`, {params})
  }
}
