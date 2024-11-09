import { NgModule } from '@angular/core';
import { ServerModule } from '@angular/platform-server';

import { AppModule } from './app.module';
import { AccountComponent } from './account/account.component';
import {SummonerComponent} from './summoner/summoner.component';

@NgModule({
  imports: [
    AppModule,
    ServerModule,
  ],
  bootstrap: [AccountComponent],
})
export class AppServerModule {}
