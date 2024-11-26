package me.riot.integration.api.ranked.rest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerChampionStats {
    private Double winPercentage;
    private Double minionsPer10;
    private String championName;
    private byte[] championImage;
}
