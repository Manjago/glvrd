package com.temnenkov.tgibot.tgapi.method;

import com.google.gson.annotations.SerializedName;
import com.temnenkov.tgibot.tgapi.dto.keyboard.ReplyMarkup;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("squid:S1068")
@Data
@Builder
public class SendMessage implements Serializable{
    /**
     * Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    @SerializedName("chat_id")
    private Long chatId;
    /**
     * Text of the message to be sent
     */
    private String text;
    /**
     * Optional
     * Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in your bot's message.
     */
    @SerializedName("parse_mode")
    private String parseMode;

    /**
     * Optional
     * Disables link previews for links in this message
     */
    @SerializedName("disable_web_page_preview")
    private Boolean disableWebPagePreview;
    /**
     * Optional
     * Sends the message silently. iOS users will not receive a notification, Android users will receive a notification
     * with no sound.
     */
    @SerializedName("disable_notification")
    private Boolean disableNotification;
    /**
     * Optional
     * If the message is a reply, ID of the original message
     */
    @SerializedName("reply_to_message_id")
    private Long replyToMessageId;

    @SerializedName("reply_markup")
    private ReplyMarkup replyMarkup;

}
