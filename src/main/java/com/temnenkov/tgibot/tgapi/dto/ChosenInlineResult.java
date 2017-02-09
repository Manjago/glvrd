package com.temnenkov.tgibot.tgapi.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("squid:S1068")
@Data
public class ChosenInlineResult implements Serializable {
    /**
     * The unique identifier for the result that was chosen
     */
    @SerializedName("result_id")
    private String resultId;
    /**
     * The user that chose the result
     */
    private User from;
    /**
     * Optional.
     * Sender location, only for bots that require user location
     */
    private Location location;
    /**
     * Optional.
     * Identifier of the sent inline message. Available only if there is an inline keyboard attached to the message.
     * Will be also received in callback queries and can be used to edit the message.
     */
    @SerializedName("inline_message_id")
    private String inlineMessageId;
    /**
     * The query that was used to obtain the result
     */
    private String query;

}
