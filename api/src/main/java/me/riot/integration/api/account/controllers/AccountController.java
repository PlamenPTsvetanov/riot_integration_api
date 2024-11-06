package me.riot.integration.api.account.controllers;

import me.riot.integration.api.account.dto.AccountDTO;
import me.riot.integration.api.account.rest.in.AccountSimpleInRestBean;
import me.riot.integration.api.account.services.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService service;


    public AccountController(AccountService service) {
        this.service = service;
    }


    @GetMapping(path = "/get")
    public AccountDTO getAccount(
            @RequestParam("username") String gameName,
            @RequestParam("tag") String tagLine) {
        return service.getAccountPUUID(new AccountSimpleInRestBean(gameName, tagLine));
    }
}

