package me.riot.integration.api.ranked.rest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantOutRestBean {
    private int kills;
    private int assists;
    private int deaths;
    private int champLevel;
    private int championId;
    private String championName;
    private int doubleKills;
    private int goldEarned;
    private byte[] item0;
    private byte[] item1;
    private byte[] item2;
    private byte[] item3;
    private byte[] item4;
    private byte[] item5;
    private byte[] item6;
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

