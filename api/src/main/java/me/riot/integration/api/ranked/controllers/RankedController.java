package me.riot.integration.api.ranked.controllers;

import me.riot.integration.api.ranked.dto.full.MatchHistoryBean;
import me.riot.integration.api.ranked.rest.PlayerChampionStats;
import me.riot.integration.api.ranked.services.RankedService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ranked")
public class RankedController {
    private final RankedService service;


    public RankedController(RankedService service) {
        this.service = service;
    }

    @GetMapping(path = "/match-history")
    public List<MatchHistoryBean> getMatchHistory(@RequestParam String puuid) {
        return service.getMatchHistory(puuid);
    }

    @GetMapping(path = "/champion-stats")
    public List<PlayerChampionStats> getChampionStats(@RequestParam String puuid) {
        return service.getChampionsWithWins(puuid);
    }
}

