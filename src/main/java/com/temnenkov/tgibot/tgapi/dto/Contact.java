package com.temnenkov.tgibot.tgapi.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@Data
public class Contact implements Serializable {
    /**
     * Contact's phone number
     */
    @SerializedName("phone_number")
    private String phoneNumber;
    /**
     * Contact's first name
     */
    @SerializedName("first_name")
    private String firstName;
    /**
     * Optional.
     * Contact's last name
     */
    @SerializedName("last_name")
    private String lastName;
    /**
     * Optional.
     * Contact's user identifier in Telegram
     */
    @SerializedName("user_id")
    private String userId;
}
