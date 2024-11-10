package me.riot.integration.api.account.services;

import me.riot.integration.api._common.services.BaseService;
import me.riot.integration.api._common.utils.HTTPMethod;
import me.riot.integration.api.account.dto.SummonerDTO;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Base64;

@Service
public class SummonerService extends BaseService<SummonerDTO> {

    private static final String CLASS_END_POINT = "summoner/v4/";
    private static final String SUMMONERS_BY_PUUID = "summoners/by-puuid/";

    private static final String ICON_END_POINT = "img/profileicon/";
    private static final String ICON_EXTENSION = ".png";

    public SummonerDTO getSummonerInfo(String accountPuuid) {
        SummonerDTO response;
        try {
            StringBuilder modifiedRequest =
                    new StringBuilder(_apiUrlEun1)
                            .append(CLASS_END_POINT)
                            .append(SUMMONERS_BY_PUUID)
                            .append(accountPuuid);


            String retrievedFromApi = super.sendRequest(modifiedRequest.toString(), HTTPMethod.GET);
            response = _objectMapper.readValue(retrievedFromApi, SummonerDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }


    public byte[] getSummonerIcon(BigInteger iconId) {
        byte[] icon;
        try {
            StringBuilder modifiedRequest =
                    new StringBuilder(_dataDragonUrl)
                            .append(ICON_END_POINT)
                            .append(iconId)
                            .append(ICON_EXTENSION);


            icon = super.sendRequestBytes(modifiedRequest.toString(), HTTPMethod.GET);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return icon;
    }
}
