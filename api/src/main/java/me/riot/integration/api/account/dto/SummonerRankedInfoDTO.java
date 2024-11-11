package me.riot.integration.api.account.dto;

import lombok.Getter;
import lombok.Setter;
import me.riot.integration.api._common.datamodel.BaseDTO;

@Getter
@Setter
public class SummonerRankedInfoDTO extends BaseDTO {
    private String leagueId;
    private String queueType;
    private String tier;
    private String rank;
    private String summonerId;
    private Integer leaguePoints;
    private Integer wins;
    private Integer losses;
    private Boolean veteran;
    private Boolean inactive;
    private Boolean freshBlood;
    private Boolean hotStreak;
}

