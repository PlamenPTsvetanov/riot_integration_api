package me.riot.integration.api.account.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import me.riot.integration.api._common.services.BaseService;
import me.riot.integration.api._common.utils.HTTPMethod;
import me.riot.integration.api.account.dto.AccountDTO;
import me.riot.integration.api.account.repositories.IAccountRepository;
import me.riot.integration.api.account.rest.in.AccountSimpleInRestBean;
import me.riot.integration.api.account.rest.orm.AccountOrmBean;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.time.Instant;
import java.time.temporal.TemporalUnit;

@Service
@Slf4j
public class AccountService extends BaseService<AccountOrmBean, AccountDTO> {
    private static final String CLASS_END_POINT = "account/v1/";
    private static final String BY_RIOT_ID_EP = "accounts/by-riot-id/";
    private final IAccountRepository repository;
    private final ModelMapper mapper;

    public AccountService(IAccountRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Retrieving account PUUID in order to execute further queries
     *
     * @param inRestBean - object wrapping the passed request params
     * @return - base account DTO
     */
    public AccountDTO getAccountPUUID(AccountSimpleInRestBean inRestBean) {
        AccountDTO response;
        try {
            AccountOrmBean orm = this.find(inRestBean.getGameName(), inRestBean.getTagLine());
            // Using some limit in order to reduce queries to api and reflect them towards local database
            Instant limit = Instant.now().minusSeconds(300);
            if (orm != null && orm.getLastCheckedDate().isBefore(limit)) {
                log.info("Found user in database and last check date is in order.");
                return mapper.map(orm, AccountDTO.class);
            } else {
                log.warn("Fetching new data.");
                String modifiedRequest = _apiUrl +
                        _BASE_END_POINT +
                        CLASS_END_POINT +
                        BY_RIOT_ID_EP +
                        URLEncoder.encode(inRestBean.getGameName()) +
                        "/" +
                        inRestBean.getTagLine();
                String retrievedFromApi = super.sendRequest(modifiedRequest, HTTPMethod.GET);
                response = _objectMapper.readValue(retrievedFromApi, AccountDTO.class);
                this.save(response);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    @Override
    protected IAccountRepository getRepository() {
        return this.repository;
    }
    @Override
    protected AccountOrmBean build(AccountDTO dto) {
        AccountOrmBean account = new AccountOrmBean();
        account.setPuuid(dto.getPuuid());
        account.setGameName(dto.getGameName());
        account.setTagLine(dto.getTagLine());
        account.setLastCheckedDate(Instant.now());
        return account;
    }
    @Transactional
    private AccountOrmBean find(String name, String tag) {
        return this.repository.getByGameNameAndTagLine(name, tag);
    }
}
