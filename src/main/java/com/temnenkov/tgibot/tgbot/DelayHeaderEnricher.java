package com.temnenkov.tgibot.tgbot;

import com.temnenkov.tgibot.tgapi.method.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;

public class DelayHeaderEnricher {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelayHeaderEnricher.class);

    private SendStat sendStat;

    private static final long DELAY = 1000L;

    public String makeDelay(SendMessage payload){

        Date now = new Date();
        final Long chatId = payload.getChatId();
        Date lastSent = sendStat.getSentDate(chatId);

        if (lastSent == null){
            LOGGER.debug("no last sent for {}", payload);
            sendStat.setSentDate(chatId, now);
            return "0";
        }

        long diff = now.getTime() - lastSent.getTime();

        if (diff < DELAY){
            long delay = DELAY - diff;
            final Date sentDate = new Date(now.getTime() + delay);
            sendStat.setSentDate(chatId, sentDate);
            LOGGER.debug("{} need delay {} ms, next date {}", payload, delay, sentDate);
            return Long.toString(delay);
        } else {
            LOGGER.debug("not need delay for {}", payload);
            sendStat.setSentDate(chatId, now);
            return "0";
        }

    }

    @Required
    public void setSendStat(SendStat sendStat) {
        this.sendStat = sendStat;
    }


}
