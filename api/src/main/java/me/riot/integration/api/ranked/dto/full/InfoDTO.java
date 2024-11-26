package me.riot.integration.api.ranked.dto.full;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InfoDTO {
    private String endOfGameResult;
    private long gameCreation;
    private long gameDuration;
    private long gameEndTimestamp;
    private long gameId;
    private String gameMode;
    private String gameName;
    private long gameStartTimestamp;
    private String gameType;
    private String gameVersion;
    private int mapId;
    private List<ParticipantDTO> participants;
    private String platformId;
    private int queueId;
    private List<TeamDTO> teams;
    private String tournamentCode;
}

