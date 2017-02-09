package com.temnenkov.tgibot.tgbot;

import com.temnenkov.statstore.JdbcStatStore;
import com.temnenkov.tgibot.tgapi.dto.Update;
import com.temnenkov.tgibot.tgapi.method.SendMessage;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

public class StatInterceptor extends ChannelInterceptorAdapter {

    private JdbcStatStore jdbcStatStore;

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        Object payload = message.getPayload();
        if (payload instanceof Update) {
            jdbcStatStore.storeUpdate((Update) payload);
        } else if (payload instanceof SendMessage) {
            jdbcStatStore.storeSendMessage((SendMessage) payload);
        }
    }

    @Required
    public void setJdbcStatStore(JdbcStatStore jdbcStatStore) {
        this.jdbcStatStore = jdbcStatStore;
    }


}
