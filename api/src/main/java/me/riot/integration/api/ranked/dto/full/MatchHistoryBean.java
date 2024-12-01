package me.riot.integration.api.ranked.dto.full;

import lombok.Getter;
import lombok.Setter;
import me.riot.integration.api._common.datamodel.MatchHolderDTO;

import java.util.UUID;

@Getter
@Setter
public class MatchHistoryBean implements MatchHolderDTO {
    private InfoDTO info;
}
