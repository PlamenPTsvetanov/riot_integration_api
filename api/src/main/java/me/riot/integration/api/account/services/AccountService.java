package me.riot.integration.api.account.services;

import lombok.extern.slf4j.Slf4j;
import me.riot.integration.api._common.services.BaseService;
import me.riot.integration.api._common.utils.HTTPMethod;
import me.riot.integration.api.account.dto.AccountDTO;
import me.riot.integration.api.account.rest.in.AccountSimpleInRestBean;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;

@Service
@Slf4j
public class AccountService extends BaseService<AccountDTO> {
    private static final String CLASS_END_POINT = "account/v1/";
    private static final String BY_RIOT_ID_EP = "accounts/by-riot-id/";

    /**
     * Retrieving account PUUID in order to execute further queries
     * @param inRestBean - object wrapping the passed request params
     * @return - base account DTO
     */
    public AccountDTO getAccountPUUID(AccountSimpleInRestBean inRestBean) {
        AccountDTO response;
        try {
            StringBuilder modifiedRequest =
                    new StringBuilder(_apiUrl)
                            .append(_BASE_END_POINT)
                            .append(CLASS_END_POINT)
                            .append(BY_RIOT_ID_EP)
                            .append(URLEncoder.encode(inRestBean.getGameName()))
                            .append("/")
                            .append(inRestBean.getTagLine());


            String retrievedFromApi = super.sendRequest(modifiedRequest.toString(), HTTPMethod.GET);
            log.info(retrievedFromApi);
            response = _objectMapper.readValue(retrievedFromApi, AccountDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
