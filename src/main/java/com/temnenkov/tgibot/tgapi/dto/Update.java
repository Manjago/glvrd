package com.temnenkov.tgibot.tgapi.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("squid:S1068")
@Data
public class Update implements Serializable {
    /**
     * The update‘s unique identifier. Update identifiers start from a certain positive number and increase
     * sequentially. This ID becomes especially handy if you’re using Webhooks, since it allows you to ignore
     * repeated updates or to restore the correct update sequence, should they get out of order.
     */
    @SerializedName("update_id")
    private Long updateId;
    /**
     * Optional.
     * New incoming message of any kind — text, photo, sticker, etc.
     */
    private Message message;
    /**
     * Optional.
     * New version of a message that is known to the bot and was edited
     */
    @SerializedName("edited_message")
    private Message editedMessage;
    /**
     * Optional.
     * New incoming inline query
     */
    @SerializedName("inline_query")
    private InlineQuery inlineQuery;
    /**
     * Optional.
     * The result of an inline query that was chosen by a user and sent to their chat partner.
     */
    @SerializedName("chosen_inline_result")
    private ChosenInlineResult chosenInlineResult;
    /**
     * Optional.
     * New incoming callback query
     */
    @SerializedName("callback_query")
    private CallbackQuery callbackQuery;

    public boolean hasMessage() {
        return this.message != null;
    }

}
