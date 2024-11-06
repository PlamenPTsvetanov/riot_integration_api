import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AccountComponent} from './account/account.component';
import {RouterModule} from '@angular/router';
import {provideHttpClient, withFetch} from '@angular/common/http';
import {TooltipModule} from 'ngx-bootstrap/tooltip';
import {FormsModule} from '@angular/forms';

@NgModule({
  declarations: [AccountComponent],
  imports: [
    BrowserModule,
    RouterModule.forRoot([]),
    TooltipModule.forRoot(),
    FormsModule,
  ],
  providers: [
    provideHttpClient(withFetch())
  ],
  bootstrap: [AccountComponent]
})
export class AppModule {
}
