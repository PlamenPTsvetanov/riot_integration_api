package me.riot.integration.api.ranked.dto.full;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObjectivesDTO {
    private ObjectiveDTO baron;
    private ObjectiveDTO champion;
    private ObjectiveDTO dragon;
    private ObjectiveDTO horde;
    private ObjectiveDTO inhibitor;
    private ObjectiveDTO riftHerald;
    private ObjectiveDTO tower;
}
