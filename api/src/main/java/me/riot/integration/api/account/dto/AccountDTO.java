/**
 * Class representing base account information for player
 */
package me.riot.integration.api.account.dto;

import lombok.*;
import me.riot.integration.api._common.datamodel.BaseDTO;

@Getter
@Setter
public class AccountDTO extends BaseDTO {
    /**
     * In-game name of the player account
     */
    private String gameName;
    /**
     * Special code for the in-game account of the player.
     * Combination of gameName and tagLine <b>must</b> be unique.
     */
    private String tagLine;
}
