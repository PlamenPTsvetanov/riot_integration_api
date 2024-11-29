import {ParticipantData} from './participant-data';


export class GameInfoAsParticipant {
  endOfGameResult: string;
  gameDuration: number;
  gameMode: string;
  gameType: string;
  mapId: number;
  participants: ParticipantData[];
  queueId: number;
}
