import {TeamObjective} from './team-objective';

export interface Team {
  bans: { championId: number; pickTurn: number }[];
  objectives: TeamObjective;
  teamId: number;
  win: boolean;
}
