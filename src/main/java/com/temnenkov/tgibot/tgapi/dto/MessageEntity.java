package com.temnenkov.tgibot.tgapi.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageEntity implements Serializable {
    /**
     * Type of the entity. Can be mention (@username), hashtag, bot_command, url, email, bold (bold text),
     * italic (italic text), code (monowidth string), pre (monowidth block), text_link (for clickable text URLs),
     * text_mention (for users without usernames)
     */
    private String type;
    /**
     * Offset in UTF-16 code units to the start of the entity
     */
    private Integer offset;
    /**
     * Length of the entity in UTF-16 code units
     */
    private Integer length;
    /**
     * Optional.
     * For “text_link” only, url that will be opened after user taps on the text
     */
    private String url;
    /**
     * Optional.
     * For “text_mention” only, the mentioned user
     */
    private User user;
}
