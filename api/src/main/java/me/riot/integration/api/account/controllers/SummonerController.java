package me.riot.integration.api.account.controllers;

import me.riot.integration.api.account.dto.SummonerDTO;
import me.riot.integration.api.account.services.SummonerService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
@RequestMapping("/summoners")
public class SummonerController {
    private final SummonerService service;


    public SummonerController(SummonerService service) {
        this.service = service;
    }


    @GetMapping(path = "")
    public SummonerDTO getSummonerMetaData(
            @RequestParam("puuid") String puuid) {
        return service.getSummonerInfo(puuid);
    }


    @GetMapping(path = "/icon", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getIcon(
            @RequestParam("iconId") BigInteger iconId) {
        return service.getSummonerIcon(iconId);
    }
}

