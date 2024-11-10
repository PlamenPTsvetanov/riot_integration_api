import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {SummonerComponent} from "./summoner/summoner.component";
import {AccountComponent} from "./account/account.component";
import {HeaderComponent} from "./header/header.component";

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [HeaderComponent, AccountComponent, SummonerComponent],
    templateUrl: './app.component.html',
    styleUrl: './app.component.css'
})
export class AppComponent {

}
