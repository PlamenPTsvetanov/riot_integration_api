package me.riot.integration.api.account.services;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import me.riot.integration.api._common.services.BaseService;
import me.riot.integration.api._common.utils.HTTPMethod;
import me.riot.integration.api.account.dto.SummonerDTO;
import me.riot.integration.api.account.dto.SummonerRankedInfoDTO;
import me.riot.integration.api.account.repositories.ISummonerRankedInfoRepository;
import me.riot.integration.api.account.repositories.ISummonerRepository;
import me.riot.integration.api.account.rest.orm.SummonerOrmBean;
import me.riot.integration.api.account.rest.orm.SummonerRankedInfoOrmBean;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class SummonerService extends BaseService<SummonerOrmBean, SummonerDTO> {

    private static final String SUMMONER_V_4 = "summoner/v4/";
    private static final String LEAGUE_V_4 = "league/v4/";
    private static final String SUMMONERS_BY_PUUID = "summoners/by-puuid/";
    private static final String ENTRIES = "entries/by-summoner/";

    private final ISummonerRepository repository;
    private final ISummonerRankedInfoRepository rankedInfoRepository;

    public SummonerService(ISummonerRepository repository, ISummonerRankedInfoRepository rankedInforepository) {
        this.repository = repository;
        this.rankedInfoRepository = rankedInforepository;
    }

    public SummonerDTO getSummonerInfo(String accountPuuid) {
        SummonerDTO response;
        try {
            SummonerOrmBean summoner = this.repository.getByPuuid(accountPuuid);

            if (summoner != null
                    && summoner.getLastCheckDate().isAfter(_fetchLimit)) {
                log.info("Found summoner in database and last check date is in order.");
                return _mapper.map(summoner, SummonerDTO.class);
            } else {
                log.warn("Fetching new data.");
                String modifiedRequest = _apiUrlEun1 +
                        SUMMONER_V_4 +
                        SUMMONERS_BY_PUUID +
                        accountPuuid;

                String retrievedFromApi = super.sendRequest(modifiedRequest, HTTPMethod.GET);
                response = _objectMapper.readValue(retrievedFromApi, SummonerDTO.class);
                this.save(response);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public SummonerRankedInfoDTO getSummonerRankedInformation(String summonerId) {
        SummonerRankedInfoDTO dto;
        try {
            SummonerRankedInfoOrmBean rankedInfo = this.rankedInfoRepository.getBySummonerId(summonerId);

            if (rankedInfo != null && rankedInfo.getLastCheckDate().isAfter(_fetchLimit)) {
                log.info("Found rankedInfo in database.");
                return _mapper.map(rankedInfo, SummonerRankedInfoDTO.class);
            } else {
                log.warn("Fetching new data.");
                String modifiedRequest = _apiUrlEun1 +
                        LEAGUE_V_4 +
                        ENTRIES +
                        summonerId;

                String content = super.sendRequest(modifiedRequest, HTTPMethod.GET);
                List<SummonerRankedInfoDTO> dtos = _objectMapper.readValue(content, new TypeReference<>() {
                });
                dto = dtos.get(0);
                SummonerRankedInfoOrmBean orm = new SummonerRankedInfoOrmBean();

                orm.setSummonerId(summonerId);
                orm.setLosses(dto.getLosses());
                orm.setWins(dto.getWins());
                orm.setTier(dto.getTier());
                orm.setLeaguePoints(dto.getLeaguePoints());
                orm.setRank(dto.getRank());
                orm.setLastCheckDate(Instant.now());
                orm.setId(rankedInfo == null ? UUID.randomUUID().toString() : rankedInfo.getId());

                this.rankedInfoRepository.save(orm);

                return dto;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected ISummonerRepository getRepository() {
        return this.repository;
    }

    @Override
    protected SummonerOrmBean build(SummonerDTO dto) {
        SummonerOrmBean orm = new SummonerOrmBean();
        orm.setId(dto.getId());
        orm.setPuuid(dto.getPuuid());
        orm.setLastCheckDate(Instant.now());
        orm.setAccountId(dto.getAccountId());
        orm.setProfileIconId(dto.getProfileIconId().intValue());
        return orm;
    }
}
