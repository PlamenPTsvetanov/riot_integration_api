package me.riot.integration.api.icon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IconRepository extends JpaRepository<IconOrmBean, UUID> {
}
