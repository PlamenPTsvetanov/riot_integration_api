import {Component} from '@angular/core';
import {SummonerService} from './summoner.service';

@Component({
  selector: 'app-summoner',
  templateUrl: './summoner.component.html',
  styleUrl: './summoner.component.css',
  standalone: true
})
export class SummonerComponent {

  constructor(private summonerService: SummonerService) {
  }
}
