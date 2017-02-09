package com.temnenkov.tgibot.tgapi.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("squid:S1068")
@Data
public class Chat implements Serializable {
    /**
     * Unique identifier for this chat. This number may be greater than 32 bits and some programming languages may
     * have difficulty/silent defects in interpreting it. But it smaller than 52 bits, so a signed 64 bit integer or
     * double-precision float type are safe for storing this identifier.
     */
    private Long id;
    /**
     * Type of chat, can be either “private”, “group”, “supergroup” or “channel”
     */
    private String type;
    /**
     * Optional.
     * Title, for channels and group chats
     */
    private String title;
    /**
     * Optional.
     * Username, for private chats, supergroups and channels if available
     */
    private String username;
    /**
     * Optional.
     * First name of the other party in a private chat
     */
    @SerializedName("first_name")
    private String firstName;
    /**
     * Optional.
     * Last name of the other party in a private chat
     */
    @SerializedName("last_name")
    private String lastName;

}
