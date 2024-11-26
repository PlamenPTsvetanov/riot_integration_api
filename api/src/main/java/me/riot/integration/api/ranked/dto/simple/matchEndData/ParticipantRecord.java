package me.riot.integration.api.ranked.dto.simple.matchEndData;

import lombok.Getter;
import lombok.Setter;

/**
 * Participant record hinting at read-only use
 * @param championId
 * @param puuid
 * @param totalMinionsKilled
 * @param win
 */
@Getter
@Setter
public class ParticipantRecord {
    private Long championId;
    private String puuid;
    private Long totalMinionsKilled;
    private Boolean win;
}
