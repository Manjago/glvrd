package com.temnenkov.statstore;

import com.temnenkov.tgibot.tgapi.dto.Update;
import com.temnenkov.tgibot.tgapi.method.SendMessage;

public interface StatStore {
    void storeUpdate(Update update);
    void storeSendMessage(SendMessage sendMessage);
}
