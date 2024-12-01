package me.riot.integration.api.ranked.rest;

import lombok.Getter;
import lombok.Setter;
import me.riot.integration.api.ranked.dto.full.ParticipantDTO;
import me.riot.integration.api.ranked.dto.full.TeamDTO;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InfoOutRestBean {

    private long gameDuration;
    private String gameMode;
    private ParticipantDTO player;
    private List<ParticipantDTO> myTeam = new ArrayList<>();
    private List<ParticipantDTO> otherTeam = new ArrayList<>();
    private List<TeamDTO> teams;

}
