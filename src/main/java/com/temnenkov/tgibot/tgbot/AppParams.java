package com.temnenkov.tgibot.tgbot;

import com.beust.jcommander.Parameter;
import lombok.Data;

@Data
public class AppParams {
    @Parameter(names = "-config", description = "config", required = true)
    private String config;
}
