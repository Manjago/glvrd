package com.temnenkov.tgibot.tgbot;

import com.temnenkov.tgibot.tgapi.dto.Update;
import org.springframework.messaging.Message;

public class UpdateCleaner {
    public boolean accept(Message<Update> updateMsg) {
        Update update = updateMsg.getPayload();
        return update.hasMessage() && update.getMessage().hasText();
    }
}
