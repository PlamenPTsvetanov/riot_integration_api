package me.riot.integration.api.ranked.dto.simple.matchEndData;

import lombok.Getter;
import lombok.Setter;

/**

 */
@Getter
@Setter
public class ParticipantBean {
    private String championName;
    private Long championId;
    private String puuid;
    private Long totalMinionsKilled; // this is only lane minions
    private Long neutralMinionsKilled; // this takes in objectives as well
    private Boolean win;
    private Long kills;
    private Long deaths;
    private Long assists;
}
