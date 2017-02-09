package com.temnenkov.tgibot.tgapi.dto;

import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("squid:S1068")
@Data
public class Location implements Serializable {
    /**
     * Longitude as defined by sender
     */
    private Double longitude;
    /**
     * Latitude as defined by sender
     */
    private Double latitude;
}
