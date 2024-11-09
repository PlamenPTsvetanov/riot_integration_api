import {Component} from '@angular/core';
import {SummonerService} from './summoner.service';

@Component({
  selector: 'summoner',
  templateUrl: './summoner.component.html',
  styleUrl: './summoner.component.css'
})
export class SummonerComponent {

  constructor(private summonerService: SummonerService) {
  }

  public getSummoner(puuid: string): void {

  }
}
