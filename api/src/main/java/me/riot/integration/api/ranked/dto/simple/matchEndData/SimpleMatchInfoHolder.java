package me.riot.integration.api.ranked.dto.simple.matchEndData;

import lombok.Getter;
import lombok.Setter;
import me.riot.integration.api._common.datamodel.MatchHolderDTO;

@Getter
@Setter
public class SimpleMatchInfoHolder implements MatchHolderDTO {
    private SimpleMatchInfoBean info;
}
