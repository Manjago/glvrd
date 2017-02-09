package com.temnenkov.tgibot.tgapi.dto.keyboard;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("squid:S1068")
@Data
@Builder
public class ReplyKeyboardHide implements ReplyMarkup, Serializable {
    /**
     * Requests clients to hide the custom keyboard
     */
    @SerializedName("hide_keyboard")
    private Boolean hideKeyboard;
    /**
     * Optional.
     * Use this parameter if you want to hide keyboard for specific users only.
     * Targets:
     * 1) users that are @mentioned in the text of the Message object;
     * 2) if the bot's message is a reply (has reply_to_message_id), sender of the original message.
     * <p>
     * Example: A user votes in a poll, bot returns confirmation message in reply to the vote and hides keyboard for
     * that user, while still showing the keyboard with poll options to users who haven't voted yet.
     */
    private Boolean selective;

    public static ReplyKeyboardHide hide() {
        return ReplyKeyboardHide.builder()
                .hideKeyboard(true)
                .build();

    }
}
