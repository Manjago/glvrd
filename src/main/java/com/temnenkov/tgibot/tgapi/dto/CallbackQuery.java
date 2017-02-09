package com.temnenkov.tgibot.tgapi.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("squid:S1068")
@Data
public class CallbackQuery implements Serializable {
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
     * Message with the callback button that originated the query. Note that message content and message date will
     * not be available if the message is too old
     */
    private Message message;
    /**
     * Optional.
     * Identifier of the message sent via the bot in inline mode, that originated the query
     */
    @SerializedName("inline_message_id")
    private String inlineMessageId;
    /**
     * Data associated with the callback button. Be aware that a bad client can send arbitrary data in this field
     */
    private String data;

}
