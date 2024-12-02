package me.riot.integration.api.account.repositories;

import me.riot.integration.api.account.rest.orm.SummonerRankedInfoOrmBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISummonerRankedInfoRepository extends JpaRepository<SummonerRankedInfoOrmBean, String> {
    SummonerRankedInfoOrmBean getBySummonerId(String id);
}