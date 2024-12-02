package me.riot.integration.api.account.controllers;

import me.riot.integration.api.account.dto.SummonerDTO;
import me.riot.integration.api.account.dto.SummonerRankedInfoDTO;
import me.riot.integration.api.account.services.SummonerService;
import me.riot.integration.api.icon.IconService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/summoners")
public class SummonerController {
    private final SummonerService service;
    private final IconService iconService;

    public SummonerController(SummonerService service, IconService iconService) {
        this.service = service;
        this.iconService = iconService;
    }


    @GetMapping(path = "")
    public SummonerDTO getSummonerMetaData(
            @RequestParam("puuid") String puuid) {
        return service.getSummonerInfo(puuid);
    }


    @GetMapping(path = "/icon", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getIcon(
            @RequestParam("iconId") BigInteger iconId) {
        return iconService.getSummonerIcon(iconId);
    }

    @GetMapping(path = "/rankedInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public SummonerRankedInfoDTO getRankedInfo(
            @RequestParam("summonerId") String summonerId) {
        return service.getSummonerRankedInformation(summonerId);
    }
}

