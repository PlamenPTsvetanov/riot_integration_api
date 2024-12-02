package me.riot.integration.api.ranked.repositories;

import me.riot.integration.api.ranked.rest.orm.MatchOrmBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMatchRepository extends JpaRepository<MatchOrmBean, String> {
}