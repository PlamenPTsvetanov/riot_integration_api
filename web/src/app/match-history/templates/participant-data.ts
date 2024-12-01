export interface ParticipantData {
  kills: number;
  assists: number;
  deaths: number;
  champLevel: number;
  championId: number;
  championName: string;
  doubleKills: number;
  goldEarned: number;
  item0: string | null;
  item1: string | null;
  item2: string | null;
  item3: string | null;
  item4: string | null;
  item5: string | null;
  item6: string | null;
  neutralMinionsKilled: number;
  participantId: number;
  profileIcon: number;
  puuid: string;
  riotIdGameName: string;
  teamId: number;
  teamPosition: string;
  totalMinionsKilled: number;
  visionScore: number;
  wardsPlaced: number;
  win: boolean;
  championImage: string;
}
