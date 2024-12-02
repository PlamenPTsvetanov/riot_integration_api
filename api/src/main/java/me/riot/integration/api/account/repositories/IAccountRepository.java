package me.riot.integration.api.account.repositories;

import me.riot.integration.api.account.rest.orm.AccountOrmBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IAccountRepository extends JpaRepository<AccountOrmBean, UUID> {
    AccountOrmBean getByGameNameAndTagLine(String accountName, String tagLine);
}
