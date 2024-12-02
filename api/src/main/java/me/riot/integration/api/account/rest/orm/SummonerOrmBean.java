package me.riot.integration.api.account.rest.orm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import me.riot.integration.api._common.datamodel.BaseOrmBean;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "summoner", schema = "riot_integration")
public class SummonerOrmBean extends BaseOrmBean {
    @Id
    @Size(max = 100)
    @Column(name = "id", nullable = false, length = 100)
    private String id;

    @Size(max = 200)
    @NotNull
    @Column(name = "puuid", nullable = false, length = 200)
    private String puuid;

    @Size(max = 100)
    @NotNull
    @Column(name = "account_id", nullable = false, length = 100)
    private String accountId;

    @NotNull
    @Column(name = "last_check_date", nullable = false)
    private Instant lastCheckDate;

}