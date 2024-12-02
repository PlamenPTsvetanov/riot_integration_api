package me.riot.integration.api.account.rest.orm;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import me.riot.integration.api._common.datamodel.BaseOrmBean;
import me.riot.integration.api.icon.IconOrmBean;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "account", schema = "riot_integration")
public class AccountOrmBean extends BaseOrmBean {
    @Id
    @Size(max = 200)
    @Column(name = "puuid", nullable = false, length = 200)
    private String puuid;

    @Size(max = 100)
    @NotNull
    @Column(name = "account_name", nullable = false, length = 100)
    private String gameName;

    @Size(max = 5)
    @NotNull
    @Column(name = "tag_line", nullable = false, length = 5)
    private String tagLine;

    @NotNull
    @Column(name = "last_checked_date", nullable = false)
    private Instant lastCheckedDate;
}