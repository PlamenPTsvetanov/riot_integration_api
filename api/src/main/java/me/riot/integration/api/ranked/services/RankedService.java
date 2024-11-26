package me.riot.integration.api.ranked.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import me.riot.integration.api._common.services.BaseService;
import me.riot.integration.api._common.utils.HTTPMethod;
import me.riot.integration.api.account.dto.AccountDTO;
import me.riot.integration.api.ranked.dto.simple.matchEndData.MatchSimpleDataHolder;
import me.riot.integration.api.ranked.dto.simple.matchEndData.ParticipantBean;
import me.riot.integration.api.ranked.dto.simple.matchEndData.SimpleMatchInfoHolder;
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
    public List<PlayerChampionStats> getChampionsWithWins() {
        // Champion id -> list of stats
        List<PlayerChampionStats> champStats = new ArrayList<>();
        try {
            List<String> lastMatches = this.getLastMatches("Rp5NE2Jlwx9v8udkxFL1H52_bY20ULWlY1YfOxg7M2l5z6D8mS5I2YD5POTiGuMcMXurkNvyE_7rCw");

            Map<MatchSimpleDataHolder, Set<ParticipantBean>> endData = new HashMap<>();

            getEndDataFromMatches(lastMatches, endData);

            for (MatchSimpleDataHolder holder : endData.keySet()) {
                Set<ParticipantBean> data = endData.get(holder);
                PlayerChampionStats stats = getPlayerChampionStats(holder, data);

                champStats.add(stats);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        champStats.sort((f, s) -> s.getGamesPlayed().compareTo(f.getGamesPlayed()));
        return champStats;
    }

    private void getEndDataFromMatches(List<String> lastMatches, Map<MatchSimpleDataHolder, Set<ParticipantBean>> endData) throws JsonProcessingException {
        for (String match : lastMatches) {
            StringBuilder modifiedRequest =
                    new StringBuilder(_apiUrl)
                            .append(CLASS_END_POINT)
                            .append(BY_MATCH_ID)
                            .append("/")
                            .append(match);


            String retrievedFromApi = super.sendRequest(modifiedRequest.toString(), HTTPMethod.GET);
            SimpleMatchInfoHolder dto = _objectMapper.readValue(retrievedFromApi, new TypeReference<>() {
            });
            // Retrieving match data for chosen player only
            dto.getInfo().setParticipants(
                    dto
                            .getInfo()
                            .getParticipants()
                            .stream()
                            .filter(
                                    e -> e.getPuuid().equals("Rp5NE2Jlwx9v8udkxFL1H52_bY20ULWlY1YfOxg7M2l5z6D8mS5I2YD5POTiGuMcMXurkNvyE_7rCw")).toList());

            // We know we have only one participant unfiltered, so we proceed without fear of NPE
            ParticipantBean currData = dto.getInfo().getParticipants().getFirst();

            // Adding simple match data with an empty set
            Long champId = currData.getChampionId();
            MatchSimpleDataHolder msdh = new MatchSimpleDataHolder();
            msdh.setChampPlayedId(champId);
            msdh.setDuration(dto.getInfo().getGameDuration());

            endData.putIfAbsent(msdh, new HashSet<>());
            endData.get(msdh).add(currData);
        }
    }

    private PlayerChampionStats getPlayerChampionStats(MatchSimpleDataHolder holder, Set<ParticipantBean> data) {
        int gamesPlayed = data.size();
        double winCounter = 0.0;
        double minionCounter = 0.0;
        double avgKills = 0.0;
        double avgAssists = 0.0;
        double avgDeaths = 0.0;
        String championName = null;
        byte[] championImageBytes = null;

        for (ParticipantBean datum : data) {
            winCounter += datum.getWin() ? 100 : 0;
            minionCounter += datum.getTotalMinionsKilled();
            minionCounter += datum.getNeutralMinionsKilled();
            avgKills += datum.getKills();
            avgAssists += datum.getAssists();
            avgDeaths += datum.getDeaths();
            if (championName == null) {
                championName = datum.getChampionName();
                championImageBytes = getChampionImageBytes(championName);
            }
        }

        PlayerChampionStats stats = new PlayerChampionStats();
        stats.setMinionsPer10(String.format("%.2f", (minionCounter / (holder.getDuration() / 60.0)) / gamesPlayed)); // seconds to minute
        stats.setWinPercentage(String.format("%.2f", winCounter / gamesPlayed));
        stats.setChampionName(championName);
        stats.setGamesPlayed(data.size());
        stats.setAssists(String.format("%.1f", avgAssists / gamesPlayed));
        stats.setDeaths(String.format("%.1f", avgDeaths / gamesPlayed));
        stats.setKills(String.format("%.1f", avgKills / gamesPlayed));
        stats.setAvgMinions(String.format("%.1f", minionCounter / gamesPlayed));
        stats.setChampionImage(championImageBytes);
        return stats;
    }

    private byte[] getChampionImageBytes(String championName) {
        StringBuilder imageRequest =
                new StringBuilder(_dataDragonUrl)
                        .append("img/")
                        .append("champion/")
                        .append(championName)
                        .append(".png");
        return super.sendRequestBytes(imageRequest.toString(), HTTPMethod.GET);
    }
}
