import {Component, DestroyRef, inject, Input, OnInit, Output} from '@angular/core';
import {SummonerRankedService} from './summoner-ranked.service';

@Component({
  selector: 'app-summoner-ranked',
  standalone: true,
  imports: [],
  templateUrl: './summoner-ranked.component.html',
  styleUrl: './summoner-ranked.component.css'
})
export class SummonerRankedComponent implements OnInit {
  private destroyRef = inject(DestroyRef);
  @Input() summonerId: string;

  @Output() imagePath: String;
  srService = inject(SummonerRankedService);

  ngOnInit(): void {
    const srSub = this.srService.getSummonerRankedInfo(this.summonerId).subscribe(
      info => {
        this.srService._summonerRankedInfo.set(info);
        console.log(info);
        const path = this.srService._summonerRankedInfo()![0].tier;
        console.log(path);

        this.imagePath = "../../assets/ranked_icons/Rank="
          + path.charAt(0).toUpperCase() + path.slice(1).toLowerCase() + ".png";

      }
    );

    this.destroyRef.onDestroy(() => {
      srSub.unsubscribe();
    });
  }
}
