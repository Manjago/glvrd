package com.temnenkov.tgibot.tgbot;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class AppParamsTest {
    @Test
    public void getConfig() throws Exception {
        AppParams p = new AppParams();
        p.setConfig("test");
        assertEquals("test", p.getConfig());
    }

}