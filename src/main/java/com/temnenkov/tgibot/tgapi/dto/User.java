package com.temnenkov.tgibot.tgapi.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("squid:S1068")
@Data
public class User implements Serializable {
    /**
     * Unique identifier for this user or bot
     */
    private Long id;
    /**
     * User‘s or bot’s first name
     */
    @SerializedName("first_name")
    private String firstName;
    /**
     * Optional.
     * User‘s or bot’s last name
     */
    @SerializedName("last_name")
    private String lastName;

}
