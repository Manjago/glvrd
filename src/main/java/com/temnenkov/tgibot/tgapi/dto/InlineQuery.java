package com.temnenkov.tgibot.tgapi.dto;

import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("squid:S1068")
@Data
public class InlineQuery implements Serializable {
    /**
     * Unique identifier for this query
     */
    private String id;
    /**
     * Sender
     */
    private User from;
    /**
     * Optional.
     * Sender location, only for bots that request user location
     */
    private Location location;
    /**
     * Text of the query (up to 512 characters)
     */
    private String query;
    /**
     * Offset of the results to be returned, can be controlled by the bot
     */
    private String offset;
}
