package me.riot.integration.api.ranked.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import me.riot.integration.api._common.datamodel.MatchHolderDTO;
import me.riot.integration.api._common.services.BaseService;
import me.riot.integration.api._common.utils.HTTPMethod;
import me.riot.integration.api.account.dto.AccountDTO;
import me.riot.integration.api.ranked.dto.full.MatchHistoryBean;
import me.riot.integration.api.ranked.dto.full.ParticipantDTO;
import me.riot.integration.api.ranked.dto.simple.matchEndData.ParticipantBean;
import me.riot.integration.api.ranked.dto.simple.matchEndData.SimpleMatchInfoHolder;
import me.riot.integration.api.ranked.rest.InfoOutRestBean;
import me.riot.integration.api.ranked.rest.MatchHistoryOutRestBean;
import me.riot.integration.api.ranked.rest.PlayerChampionStats;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RankedService extends BaseService<AccountDTO> {
    private static final String CLASS_END_POINT = "lol/match/v5/";
    private static final String BY_PUUID = "matches/by-puuid/";
    private static final String BY_MATCH_ID = "matches";

    //Rp5NE2Jlwx9v8udkxFL1H52_bY20ULWlY1YfOxg7M2l5z6D8mS5I2YD5POTiGuMcMXurkNvyE_7rCw
    public List<PlayerChampionStats> getChampionsWithWins(String puuid) {
        // Champion id -> list of stats
        List<PlayerChampionStats> champStats = new ArrayList<>();
        try {
            List<String> lastMatches = this.getLast10MatchIds(puuid);

            Map<Long, Set<ParticipantBean>> endData = new HashMap<>();

            getEndDataForPlayerFromMatches(puuid, lastMatches, endData);

            for (Set<ParticipantBean> data : endData.values()) {
                champStats.add(getPlayerChampionStats(data));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        champStats.sort((f, s) -> s.getGamesPlayed().compareTo(f.getGamesPlayed()));
        return champStats;
    }

    public List<MatchHistoryOutRestBean> getMatchHistory(String puuid) {
        List<MatchHistoryOutRestBean> endData = new ArrayList<>();
        try {
            List<String> matches = this.getLast10MatchIds(puuid);
            for (String match : matches) {
                MatchHistoryOutRestBean outRestBean = new MatchHistoryOutRestBean();
                MatchHistoryBean data = this.getEndMatchData(match, MatchHistoryBean.class);

                // Retrieving player with other participants
                List<ParticipantDTO> otherParticipants = getPlayerWithOtherParticipants(puuid, outRestBean, data);

                // For ease of use
                InfoOutRestBean matchInfo = outRestBean.getInfo();

                // Getting player champion image
                matchInfo.getPlayer().setChampionImage(this.getChampionImageBytes(matchInfo.getPlayer().getChampionName()));
                // Getting other participants champion images
                for (ParticipantDTO otherParticipant : otherParticipants) {
                    otherParticipant.setChampionImage(this.getChampionImageBytes(otherParticipant.getChampionName()));
                }

                // Splitting other participants into my team and other team
                for (ParticipantDTO otherParticipant : otherParticipants) {
                    if (otherParticipant.getTeamId() == matchInfo.getPlayer().getTeamId()) {
                        matchInfo.getMyTeam().add(otherParticipant);
                    } else {
                        matchInfo.getOtherTeam().add(otherParticipant);
                    }
                }

                // Adding player to my team
                matchInfo.getMyTeam().add(matchInfo.getPlayer());

                matchInfo.setMyTeam(matchInfo.getMyTeam().stream()
                        .sorted(Comparator.comparingInt(p -> getPositionPriority(p.getTeamPosition())))
                        .collect(Collectors.toList()));

                matchInfo.setOtherTeam(matchInfo.getOtherTeam().stream()
                        .sorted(Comparator.comparingInt(p -> getPositionPriority(p.getTeamPosition())))
                        .collect(Collectors.toList()));

                // Hard-coded by riot
                if (data.getInfo().getQueueId() == 420) {
                    matchInfo.setGameMode("Ranked 5 vs 5");
                }
                // Adding other params
                matchInfo.setTeams(data.getInfo().getTeams());
                matchInfo.setGameDuration(data.getInfo().getGameDuration());
                endData.add(outRestBean);
            }
        } catch (Exception e) {
            // TODO gotta fix these and add logging at some point
            throw new RuntimeException(e);
        }

        return endData;
    }

    private List<ParticipantDTO> getPlayerWithOtherParticipants(String puuid, MatchHistoryOutRestBean outRestBean, MatchHistoryBean data) {
        outRestBean.setInfo(new InfoOutRestBean());
        List<ParticipantDTO> participants = data.getInfo().getParticipants();

        ParticipantDTO player = participants.stream()
                .filter(e -> e.getPuuid().equals(puuid))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Player not found"));

        List<ParticipantDTO> otherParticipants = participants.stream()
                .filter(e -> !e.getPuuid().equals(puuid))
                .collect(Collectors.toList());

        outRestBean.setInfo(new InfoOutRestBean());
        outRestBean.getInfo().setPlayer(player);
        return otherParticipants;
    }

    /**
     * Retrieves the ids of the last 10 (hardcoded) matches in order
     * to display champion stats and match history.
     *
     * @param puuid - played id
     * @return List of ids
     */
    private List<String> getLast10MatchIds(String puuid) {
        List<String> response;
        try {
            StringBuilder modifiedRequest =
                    new StringBuilder(_apiUrl)
                            .append(CLASS_END_POINT)
                            .append(BY_PUUID)
                            .append(puuid)
                            .append("/ids")
                            .append("?start=0&count=1");


            String retrievedFromApi = super.sendRequest(modifiedRequest.toString(), HTTPMethod.GET);
            response = _objectMapper.readValue(retrievedFromApi, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    private void getEndDataForPlayerFromMatches(String puuid, List<String> lastMatches, Map<Long, Set<ParticipantBean>> endData) throws JsonProcessingException {
        for (String match : lastMatches) {
            SimpleMatchInfoHolder dto = getEndMatchData(match, SimpleMatchInfoHolder.class);
            // Retrieving match data for chosen player only
            dto.getInfo().setParticipants(
                    dto
                            .getInfo()
                            .getParticipants()
                            .stream()
                            .filter(
                                    e -> e.getPuuid().equals(puuid)).toList());

            // We know we have only one participant unfiltered, so we proceed without fear of NPE
            ParticipantBean currData = dto.getInfo().getParticipants().get(0);

            currData.setGameDuration(dto.getInfo().getGameDuration());

            // Adding simple match data with an empty set
            Long champId = currData.getChampionId();

            endData.putIfAbsent(champId, new HashSet<>());
            endData.get(champId).add(currData);
        }
    }

    private <T extends MatchHolderDTO> T getEndMatchData(String match, Class<T> clazz) throws JsonProcessingException {
        String modifiedRequest = _apiUrl +
                CLASS_END_POINT +
                BY_MATCH_ID +
                "/" +
                match;
        String retrievedFromApi = super.sendRequest(modifiedRequest, HTTPMethod.GET);
        return _objectMapper.readValue(retrievedFromApi, clazz);
    }

    private PlayerChampionStats getPlayerChampionStats(Set<ParticipantBean> data) {
        int gamesPlayed = data.size();
        double winCounter = 0.0;
        double minionCounter = 0.0;
        double avgKills = 0.0;
        double avgAssists = 0.0;
        double avgDeaths = 0.0;
        double avgDuration = 0.0;
        String championName = null;
        byte[] championImageBytes = null;

        for (ParticipantBean datum : data) {
            winCounter += datum.getWin() ? 100 : 0;
            minionCounter += datum.getTotalMinionsKilled();
            minionCounter += datum.getNeutralMinionsKilled();
            avgKills += datum.getKills();
            avgAssists += datum.getAssists();
            avgDeaths += datum.getDeaths();
            avgDuration += datum.getGameDuration();
            if (championName == null) {
                championName = datum.getChampionName();
                championImageBytes = getChampionImageBytes(championName);
            }
        }

        PlayerChampionStats stats = new PlayerChampionStats();
        stats.setMinionsPer10(String.format("%.2f", (minionCounter / ((avgDuration / gamesPlayed) / 60.0)) / gamesPlayed)); // seconds to minute
        stats.setWinPercentage(String.format("%.0f", winCounter / gamesPlayed));
        stats.setChampionName(championName);
        stats.setGamesPlayed(data.size());
        stats.setAssists(String.format("%.1f", avgAssists / gamesPlayed));
        stats.setDeaths(String.format("%.1f", avgDeaths / gamesPlayed));
        stats.setKills(String.format("%.1f", avgKills / gamesPlayed));
        stats.setAvgMinions(String.format("%.1f", minionCounter / gamesPlayed));
        stats.setChampionImage(championImageBytes);
        stats.setKda(String.format("%.1f", ((avgKills + avgAssists) / avgDeaths) / gamesPlayed));
        return stats;
    }

    private byte[] getChampionImageBytes(String championName) {
        String imageRequest = _dataDragonUrl +
                "img/" +
                "champion/" +
                championName +
                ".png";
        return super.sendRequestBytes(imageRequest, HTTPMethod.GET);
    }

    private int getPositionPriority(String teamPosition) {
        return switch (teamPosition) {
            case "TOP" -> 1;
            case "JUNGLE" -> 2;
            case "MIDDLE" -> 3;
            case "BOTTOM" -> 4;
            case "UTILITY" -> 5;
            default -> Integer.MAX_VALUE; // Shouldn't happen
        };
    }

}

