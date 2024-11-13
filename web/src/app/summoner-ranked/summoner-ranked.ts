import * as string_decoder from 'node:string_decoder';
import {stringify} from 'node:querystring';

export class SummonerRanked {
  leagueId: String;
  queueType: String;
  tier: String;
  rank: String;
  summonerId: String;
  leaguePoints: number;
  wins: number;
  losses: number;
  veteran: boolean;
  inactive: boolean;
  freshBlood: boolean;
  hotstreak: boolean;
  winrate: string;
}

