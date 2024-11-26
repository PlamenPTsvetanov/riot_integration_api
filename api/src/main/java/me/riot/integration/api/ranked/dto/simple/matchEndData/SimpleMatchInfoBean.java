package me.riot.integration.api.ranked.dto.simple.matchEndData;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class SimpleMatchInfoBean {
    List<ParticipantRecord> participants;
    Long gameDuration;
    Long queueId;

}
