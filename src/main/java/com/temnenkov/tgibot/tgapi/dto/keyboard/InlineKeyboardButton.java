package com.temnenkov.tgibot.tgapi.dto.keyboard;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

/**
 * This object represents one button of an inline keyboard. You must use exactly one of the optional fields.
 */
@SuppressWarnings("squid:S1068")
@Data
public class InlineKeyboardButton implements Serializable {
    /**
     * Label text on the button
     */
    private String text;
    /**
     * Optional.
     * HTTP url to be opened when button is pressed
     */
    private String url;
    /**
     * Optional.
     * Data to be sent in a callback query to the bot when button is pressed, 1-64 bytes
     */
    @SerializedName("callback_data")
    private String callbackData;
    /**
     * Optional.
     * If set, pressing the button will prompt the user to select one of their chats, open that chat and insert
     * the bot‘s username and the specified inline query in the input field. Can be empty, in which case just
     * the bot’s username will be inserted.
     *
     *  Note: This offers an easy way for users to start using your bot in inline mode when they are currently in
     *  a private chat with it. Especially useful when combined with switch_pm… actions – in this case the user
     *  will be automatically returned to the chat they switched from, skipping the chat selection screen.
     */
    @SerializedName("switch_inline_query")
    private String switchInlineQuery;
}
