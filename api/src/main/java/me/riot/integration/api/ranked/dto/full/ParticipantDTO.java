package me.riot.integration.api.ranked.dto.full;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantDTO {
    private int kills;
    private int assists;
    private int deaths;
    private int champLevel;
    private int championId;
    private String championName;
//    private ChallengesDTO challenges;
    private int doubleKills;
    private int goldEarned;
    private int item0;
    private int item1;
    private int item2;
    private int item3;
    private int item4;
    private int item5;
    private int item6;
    private int neutralMinionsKilled;
    private int participantId;
    private int profileIcon;
    private String puuid;
    private String riotIdGameName;
    private int teamId;
    private String teamPosition;
    private int totalMinionsKilled;
    private int visionScore;
    private int wardsPlaced;
    private boolean win;
    private byte[] championImage;
}

