import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AccountComponent} from './account/account.component';
import {SummonerComponent} from './summoner/summoner.component';
import {AppComponent} from './app.component';

const routes: Routes = [
  {
    path: '', component: AppComponent,
    children: [
      {path: 'account', component: AccountComponent},
      {path: 'summoner', component: SummonerComponent},
    ]
  },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
