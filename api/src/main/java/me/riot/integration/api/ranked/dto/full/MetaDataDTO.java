package me.riot.integration.api.ranked.dto.full;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MetaDataDTO {
    private String dataVersion;
    private String matchId;
    private List<String> participants;

}

