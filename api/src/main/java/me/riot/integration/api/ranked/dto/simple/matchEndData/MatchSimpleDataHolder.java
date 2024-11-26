package me.riot.integration.api.ranked.dto.simple.matchEndData;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class MatchSimpleDataHolder {
    private Long champPlayedId;
    private Long duration;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof MatchSimpleDataHolder o1)) {
            return false;
        }

        return this.champPlayedId.equals(o1.champPlayedId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(champPlayedId);
    }
}
