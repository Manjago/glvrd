package com.temnenkov.tgibot.tgbot;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SendStat {
    private final ConcurrentMap<Long, Date> stat = new ConcurrentHashMap<>();

    public Date getSentDate(Long chatId) {
        return stat.get(chatId);
    }

    public void setSentDate(Long chatId, Date sentDate){
        stat.put(chatId, sentDate);
    }
}
