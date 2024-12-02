package me.riot.integration.api.account.repositories;

import me.riot.integration.api.account.rest.orm.SummonerOrmBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ISummonerRepository extends JpaRepository<SummonerOrmBean, String> {
    SummonerOrmBean getByPuuid(String puuid);
}
