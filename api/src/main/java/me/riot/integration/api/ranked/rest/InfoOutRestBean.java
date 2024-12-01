package me.riot.integration.api.ranked.rest;

import lombok.Getter;
import lombok.Setter;
import me.riot.integration.api.ranked.dto.full.TeamDTO;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InfoOutRestBean {

    private long gameDuration;
    private String gameMode;
    private ParticipantOutRestBean player;
    private List<ParticipantOutRestBean> myTeam = new ArrayList<>();
    private List<ParticipantOutRestBean> otherTeam = new ArrayList<>();
    private List<TeamDTO> teams;

}
