package me.riot.integration.api.account.rest.orm;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import me.riot.integration.api._common.datamodel.BaseOrmBean;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "summoner_ranked_info", schema = "riot_integration")
public class SummonerRankedInfoOrmBean extends BaseOrmBean {
    @Id
    @Size(max = 36)
    @Column(name = "id", nullable = false, length = 36)
    private String id;
    @Size(max = 36)
    @Column(name = "summoner_id", nullable = false, length = 36)
    private String summonerId;


    @Size(max = 20)
    @NotNull
    @Column(name = "tier", nullable = false, length = 20)
    private String tier;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "summoner_id", nullable = false, insertable = false, updatable = false)
    private SummonerOrmBean summoner;

    @NotNull
    @Column(name = "league_points", nullable = false)
    private Integer leaguePoints;

    @NotNull
    @Column(name = "wins", nullable = false)
    private Integer wins;

    @NotNull
    @Column(name = "losses", nullable = false)
    private Integer losses;

    @NotNull
    @Column(name = "last_check_date", nullable = false)
    private Instant lastCheckDate;

}