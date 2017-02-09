package com.temnenkov.tgibot.tgapi.dto.keyboard;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class KeyboardButton implements Serializable{
    private String text;
    @SerializedName("request_contact")
    private Boolean requestContact;
    @SerializedName("request_location")
    private Boolean requestLocation;
}
