package me.riot.integration.api.ranked.controllers;

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


    @GetMapping(path = "")
    public List<String> getMatches(
            @RequestParam("puuid") String puuid) {
        return service.getLastMatches(puuid);
    }

    @GetMapping(path = "/match")
    public List<PlayerChampionStats> getSingleMatch(
            @RequestParam("id") String id) {
        return service.getChampionsWithWins(id);
    }
}

