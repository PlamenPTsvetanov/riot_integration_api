import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AccountComponent} from './account/account.component';
import {RouterModule} from '@angular/router';
import {provideHttpClient, withFetch} from '@angular/common/http';
import {TooltipModule} from 'ngx-bootstrap/tooltip';
import {FormsModule} from '@angular/forms';
import {SummonerComponent} from './summoner/summoner.component';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';

@NgModule({
  declarations: [AccountComponent, SummonerComponent, AppComponent],
  imports: [
    BrowserModule,
    RouterModule.forRoot([]),
    TooltipModule.forRoot(),
    FormsModule,
    AppRoutingModule,
  ],
  providers: [
    provideHttpClient(withFetch())
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
