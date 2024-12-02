package me.riot.integration.api.ranked.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import me.riot.integration.api._common.datamodel.BaseDTO;
import me.riot.integration.api._common.datamodel.BaseOrmBean;
import me.riot.integration.api._common.datamodel.MatchHolderDTO;
import me.riot.integration.api._common.services.BaseService;
import me.riot.integration.api._common.utils.HTTPMethod;
import me.riot.integration.api.icon.IIconRepository;
import me.riot.integration.api.icon.IconOrmBean;
import me.riot.integration.api.icon.IconType;
import me.riot.integration.api.ranked.dto.full.MatchHistoryBean;
import me.riot.integration.api.ranked.dto.full.ParticipantDTO;
import me.riot.integration.api.ranked.dto.simple.matchEndData.ParticipantBean;
import me.riot.integration.api.ranked.dto.simple.matchEndData.SimpleMatchInfoHolder;
import me.riot.integration.api.ranked.repositories.IMatchRepository;
import me.riot.integration.api.ranked.rest.orm.MatchOrmBean;
import me.riot.integration.api.ranked.rest.out.InfoOutRestBean;
import me.riot.integration.api.ranked.rest.out.MatchHistoryOutRestBean;
import me.riot.integration.api.ranked.rest.out.ParticipantOutRestBean;
import me.riot.integration.api.ranked.rest.out.PlayerChampionStats;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RankedService extends BaseService {
    private static final String CLASS_END_POINT = "lol/match/v5/";
    private static final String BY_PUUID = "matches/by-puuid/";
    private static final String BY_MATCH_ID = "matches";
    private final ModelMapper modelMapper;

    private final IMatchRepository matchRepo;
    private final IIconRepository iconRepo;

    public RankedService(ModelMapper modelMapper, IMatchRepository matchRepo, IIconRepository iconRepo) {
        this.modelMapper = modelMapper;
        this.matchRepo = matchRepo;
        this.iconRepo = iconRepo;
    }

    /**
     * Retrieving information obout player's performance on
     * his last played champs (in the last 10 played games).
     *
     * @param puuid
     * @return
     */
    //Rp5NE2Jlwx9v8udkxFL1H52_bY20ULWlY1YfOxg7M2l5z6D8mS5I2YD5POTiGuMcMXurkNvyE_7rCw
    public List<PlayerChampionStats> getChampionsWithWins(String puuid) {
        // Champion id -> list of stats
        List<PlayerChampionStats> champStats = new ArrayList<>();
        try {
            List<String> matchIds = this.getLast10MatchIds(puuid);

            Map<Long, Set<ParticipantBean>> endData = new HashMap<>();
            getEndDataForPlayerFromMatches(puuid, matchIds, endData);

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
                List<ParticipantOutRestBean> otherParticipants = getPlayerWithOtherParticipants(puuid, outRestBean, data);

                // For ease of use
                InfoOutRestBean matchInfo = outRestBean.getInfo();

                // Getting player champion image
                matchInfo.getPlayer().setChampionImage(this.getChampionImageBytes(matchInfo.getPlayer().getChampionName()));
                // Getting other participants champion images
                for (ParticipantOutRestBean otherParticipant : otherParticipants) {
                    otherParticipant.setChampionImage(this.getChampionImageBytes(otherParticipant.getChampionName()));
                }

                // Splitting other participants into my team and other team
                for (ParticipantOutRestBean otherParticipant : otherParticipants) {
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

    private void manageItemIcons(ParticipantDTO p, ParticipantOutRestBean o) {
        o.setItem0(p.getItem0() == 0 ? null : this.getItemImageBytes(p.getItem0()));
        o.setItem1(p.getItem1() == 0 ? null : this.getItemImageBytes(p.getItem1()));
        o.setItem2(p.getItem2() == 0 ? null : this.getItemImageBytes(p.getItem2()));
        o.setItem3(p.getItem3() == 0 ? null : this.getItemImageBytes(p.getItem3()));
        o.setItem4(p.getItem4() == 0 ? null : this.getItemImageBytes(p.getItem4()));
        o.setItem5(p.getItem5() == 0 ? null : this.getItemImageBytes(p.getItem5()));
        o.setItem6(p.getItem6() == 0 ? null : this.getItemImageBytes(p.getItem6()));
    }

    private List<ParticipantOutRestBean> getPlayerWithOtherParticipants(String puuid, MatchHistoryOutRestBean outRestBean, MatchHistoryBean data) {
        outRestBean.setInfo(new InfoOutRestBean());
        List<ParticipantDTO> participants = data.getInfo().getParticipants();

        ParticipantDTO player = participants.stream()
                .filter(e -> e.getPuuid().equals(puuid))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Player not found"));

        List<ParticipantDTO> otherParticipants = participants.stream()
                .filter(e -> !e.getPuuid().equals(puuid))
                .toList();

        outRestBean.setInfo(new InfoOutRestBean());
        ParticipantOutRestBean map = modelMapper.map(player, ParticipantOutRestBean.class);
        manageItemIcons(player, map);
        outRestBean.getInfo().setPlayer(map);

        List<ParticipantOutRestBean> beans = new ArrayList<>();
        for (ParticipantDTO otherParticipant : otherParticipants) {
            ParticipantOutRestBean out = getMap(otherParticipant);
            manageItemIcons(otherParticipant, out);
            beans.add(out);
        }

        return beans;
    }

    private ParticipantOutRestBean getMap(ParticipantDTO otherParticipant) {
        return modelMapper.map(otherParticipant, ParticipantOutRestBean.class);
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
            String modifiedRequest = _apiUrl +
                    CLASS_END_POINT +
                    BY_PUUID +
                    puuid +
                    "/ids" +
                    "?start=0&count=10";
            String retrievedFromApi = super.sendRequest(modifiedRequest, HTTPMethod.GET);
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

    @Transactional
    private <T extends MatchHolderDTO> T getEndMatchData(String matchId, Class<T> clazz) throws JsonProcessingException {
        // Getting from database or api
        Optional<MatchOrmBean> potential = this.matchRepo.findById(matchId);
        if (potential.isEmpty()) {
            log.warn("Fetching new data.");
            String modifiedRequest = _apiUrl +
                    CLASS_END_POINT +
                    BY_MATCH_ID +
                    "/" +
                    matchId;
            String retrievedFromApi = super.sendRequest(modifiedRequest, HTTPMethod.GET);

            MatchOrmBean orm = new MatchOrmBean();
            orm.setId(matchId);
            orm.setData(retrievedFromApi);
            this.matchRepo.save(orm);
            return _objectMapper.readValue(retrievedFromApi, clazz);
        } else {
            log.info("Game end data found in database.");
        }
        return _objectMapper.readValue(potential.get().getData(), clazz);

    }

    @Transactional
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

    @Transactional
    private byte[] getChampionImageBytes(String championName) {
        IconOrmBean icon = iconRepo.getByChampionNameAndType(championName, IconType.CHAMPION.toString());
        if (icon == null) {
            String imageRequest = _dataDragonUrl +
                    "img/" +
                    "champion/" +
                    championName +
                    ".png";
            byte[] bytes = super.sendRequestBytes(imageRequest, HTTPMethod.GET);

            IconOrmBean orm = new IconOrmBean();
            orm.setId(UUID.randomUUID().toString());
            orm.setImage(bytes);
            orm.setRiotId(null);
            orm.setChampionName(championName);
            orm.setType(IconType.CHAMPION.toString());

            this.iconRepo.save(orm);

            return bytes;
        }
        return icon.getImage();
    }

    private byte[] getItemImageBytes(Integer itemId) {
        String imageRequest = _dataDragonUrl +
                "img/" +
                "item/" +
                itemId +
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

    @Override
    protected JpaRepository getRepository() {
        return null;
    }

    @Override
    protected BaseOrmBean build(BaseDTO dto) {
        return null;
    }
}

