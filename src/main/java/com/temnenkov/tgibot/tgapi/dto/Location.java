package com.temnenkov.tgibot.tgapi.dto;

import lombok.Data;

import java.io.Serializable;

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
