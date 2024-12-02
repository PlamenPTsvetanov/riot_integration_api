package me.riot.integration.api.ranked.rest.out;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerChampionStats {
    private String winPercentage;
    private String avgMinions;
    private String minionsPer10;
    private String championName;
    private byte[] championImage;
    private Integer gamesPlayed;
    private String kills;
    private String deaths;
    private String assists;
    private String kda;
}
