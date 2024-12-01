import {ParticipantData} from './participant-data';


export class GameInfoAsParticipant {
  gameDuration: number;
  gameMode: string;
  player: ParticipantData;
  myTeam: ParticipantData[];
  otherTeam: ParticipantData[];
}
