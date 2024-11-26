package me.riot.integration.api.ranked.dto.full;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamDTO {
    private List<BanDTO> bans;
    private ObjectivesDTO objectives;
    private int teamId;
    private boolean win;
}
