package me.riot.integration.api.account.dto;

import lombok.Getter;
import lombok.Setter;
import me.riot.integration.api._common.datamodel.BaseDTO;

import java.math.BigInteger;
import java.time.Instant;

@Getter
@Setter
public class SummonerDTO extends BaseDTO {
    private String id;
    private String accountId;
    private BigInteger profileIconId;
    private Instant revisionDate;
    private BigInteger summonerLevel;
}
