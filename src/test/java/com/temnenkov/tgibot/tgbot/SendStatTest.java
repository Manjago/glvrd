package com.temnenkov.tgibot.tgbot;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class SendStatTest {
    @Test
    public void getSentDate() throws Exception {
        SendStat s = new SendStat();
        final Date sentDate = new Date();
        s.setSentDate(1L, sentDate);
        assertEquals(sentDate, s.getSentDate(1L));
        assertNull(s.getSentDate(2L));
    }

}