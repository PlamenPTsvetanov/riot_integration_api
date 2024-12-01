package me.riot.integration.api.ranked.rest;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MatchHistoryOutRestBean {
    private InfoOutRestBean info;
    private UUID uuid = UUID.randomUUID(); // adding some system identifier, so I can traverse it in the front-end
}
