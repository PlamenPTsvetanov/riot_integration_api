/**
 * Parent class for all DTO objects used when communicating with RIOT api
 */
package me.riot.integration.api._common.datamodel;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public abstract class BaseDTO implements Serializable {
    /**
     * Unique identifier for all DTOs
     */
    private String puuid;
}
