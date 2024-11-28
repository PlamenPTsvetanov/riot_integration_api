package me.riot.integration.api.ranked.dto.full;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InfoDTO {
    private String endOfGameResult;
    private long gameDuration;
    private String gameMode;
    private String gameType;
    private int mapId;
    private List<ParticipantDTO> participants;
    private int queueId;
    private List<TeamDTO> teams;
}

