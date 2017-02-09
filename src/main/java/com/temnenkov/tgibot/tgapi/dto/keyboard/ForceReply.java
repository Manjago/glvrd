package com.temnenkov.tgibot.tgapi.dto.keyboard;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@Data
public class ForceReply implements ReplyMarkup, Serializable {
    /**
     * Shows reply interface to the user, as if they manually selected the bot‘s message and tapped ’Reply'
     */
    @SerializedName("force_reply")
    private Boolean forceReply;
    /**
     * Optional.
     * Use this parameter if you want to force reply from specific users only.
     * Targets:
     * 1) users that are @mentioned in the text of the Message object;
     * 2) if the bot's message is a reply (has reply_to_message_id), sender of the original message.
     */
    private Boolean selective;
}
