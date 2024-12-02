package me.riot.integration.api.icon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IIconRepository extends JpaRepository<IconOrmBean, UUID> {

    IconOrmBean getByRiotIdAndType(Integer riotId, String type);
    IconOrmBean getByChampionNameAndType(String championName, String type);
}
