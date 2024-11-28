package me.riot.integration.api.ranked.dto.full;

import lombok.Getter;
import lombok.Setter;
import me.riot.integration.api._common.datamodel.MatchHolderDTO;

@Getter
@Setter
public class FullEndDataHolder implements MatchHolderDTO {
    private InfoDTO info;
}
