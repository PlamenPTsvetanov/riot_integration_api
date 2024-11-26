package me.riot.integration.api.ranked.services;

import com.fasterxml.jackson.core.type.TypeReference;
import me.riot.integration.api._common.services.BaseService;
import me.riot.integration.api._common.utils.HTTPMethod;
import me.riot.integration.api.account.dto.AccountDTO;
import me.riot.integration.api.ranked.dto.simple.matchEndData.MatchMetaData;
import me.riot.integration.api.ranked.dto.simple.matchEndData.MatchSimpleDataHolder;
import me.riot.integration.api.ranked.dto.simple.matchEndData.ParticipantRecord;
import me.riot.integration.api.ranked.dto.simple.matchEndData.SimpleMatchInfoBean;
import me.riot.integration.api.ranked.rest.PlayerChampionStats;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RankedService extends BaseService<AccountDTO> {
    private static final String CLASS_END_POINT = "lol/match/v5/";
    private static final String BY_PUUID = "matches/by-puuid/";
    private static final String BY_MATCH_ID = "matches";

    public List<String> getLastMatches(String puuid) {
        List<String> response;
        try {
            StringBuilder modifiedRequest =
                    new StringBuilder(_apiUrl)
                            .append(CLASS_END_POINT)
                            .append(BY_PUUID)
                            .append(puuid)
                            .append("/ids")
                            .append("?start=0&count=10");


            String retrievedFromApi = super.sendRequest(modifiedRequest.toString(), HTTPMethod.GET);
            response = _objectMapper.readValue(retrievedFromApi, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    //Rp5NE2Jlwx9v8udkxFL1H52_bY20ULWlY1YfOxg7M2l5z6D8mS5I2YD5POTiGuMcMXurkNvyE_7rCw
    public Set<PlayerChampionStats> getChampionsWithWins(String matchId) {
        // Champion id -> list of stats
        Set<PlayerChampionStats> champStats = new HashSet<>();
        try {
            List<String> lastMatches = this.getLastMatches("Rp5NE2Jlwx9v8udkxFL1H52_bY20ULWlY1YfOxg7M2l5z6D8mS5I2YD5POTiGuMcMXurkNvyE_7rCw");

            Map<MatchSimpleDataHolder, Set<ParticipantRecord>> endData = new HashMap<>();

            for (String match : lastMatches) {
                StringBuilder modifiedRequest =
                        new StringBuilder(_apiUrl)
                                .append(CLASS_END_POINT)
                                .append(BY_MATCH_ID)
                                .append("/")
                                .append(match);


                String retrievedFromApi = super.sendRequest(modifiedRequest.toString(), HTTPMethod.GET);
                MatchMetaData dto = _objectMapper.readValue(retrievedFromApi, new TypeReference<>() {
                });
                // Retrieving match data for chosen player only
                dto.getInfo().setParticipants(
                        dto
                                .getInfo()
                                .getParticipants()
                                .stream()
                                .filter(
                                        e -> e.getPuuid().equals("Rp5NE2Jlwx9v8udkxFL1H52_bY20ULWlY1YfOxg7M2l5z6D8mS5I2YD5POTiGuMcMXurkNvyE_7rCw")).toList());
                //TODO filter by queue RANKED5x5

                // We know we have only one participant unfiltered, so we proceed without fear of NPE
                ParticipantRecord currData = dto.getInfo().getParticipants().get(0);

                // Adding simple match data with an empty set
                Long champId = currData.getChampionId();
                MatchSimpleDataHolder msdh = new MatchSimpleDataHolder();
                msdh.setChampPlayedId(champId);
                msdh.setDuration(dto.getInfo().getGameDuration());

                endData.putIfAbsent(msdh, new HashSet<>());
                endData.get(msdh).add(currData);
            }


            for (MatchSimpleDataHolder holder : endData.keySet()) {
                Set<ParticipantRecord> data = endData.get(holder);
                double currentWin = 0.0;
                double currentMPer10 = 0.0;

                for (ParticipantRecord datum : data) {
                    currentWin += datum.getWin() ? 100 : -100;
                    currentMPer10 += datum.getTotalMinionsKilled();
                }

                PlayerChampionStats stats = new PlayerChampionStats();
                stats.setMinionsPer10((currentMPer10 / (holder.getDuration() / 60)) * 10);
                stats.setWinPercentage(Math.max(currentWin / data.size(), 0));

                champStats.add(stats);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return champStats;
    }
}
