package com.temnenkov.tgibot.tgapi.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("squid:S1068")
@Data
public class Message implements Serializable {
    /**
     * Unique message identifier
     */
    @SerializedName("message_id")
    private Long messageId;
    /**
     * Optional. Sender, can be empty for messages sent to channels
     */
    private User from;
    /**
     * Date the message was sent in Unix time
     */
    private Long date;
    /**
     * Conversation the message belongs to
     */
    private Chat chat;
    /**
     * Optional.
     * For forwarded messages, sender of the original message
     */
    @SerializedName("forward_from")
    private User forwardFrom;
    /**
     * Optional.
     * For messages forwarded from a channel, information about the original channel
     */
    @SerializedName("forward_from_chat")
    private Chat forwardFromChat;
    /**
     * Optional.
     * For forwarded messages, date the original message was sent in Unix time
     */
    @SerializedName("forward_date")
    private Long forwardDate;
    /**
     * Optional.
     * For replies, the original message. Note that the Message object in this field will not contain further
     * reply_to_message fields even if it itself is a reply.
     */
    @SerializedName("reply_to_message")
    private Message replyToMessage;
    /**
     * Optional.
     * Date the message was last edited in Unix time
     */
    @SerializedName("edit_date")
    private Long editDate;
    /**
     * Optional.
     * For text messages, the actual UTF-8 text of the message, 0-4096 characters.
     */
    private String text;
    /**
     * Optional. For text messages, special entities like usernames, URLs, bot commands, etc. that appear in the text
     */
    private MessageEntity[] entities;
    /**
     * Optional.
     * Message is a shared contact, information about the contact
     */
    private Contact contact;

    public boolean hasText() {
        return this.text != null && !this.text.isEmpty();
    }

    public boolean isCommand() {
        if (hasText() && entities != null) {
            for (MessageEntity entity : entities) {
                if (entity != null && entity.getOffset() == 0 &&
                        EntityType.BOTCOMMAND.equals(entity.getType())) {
                    return true;
                }
            }
        }
        return false;
    }

}
