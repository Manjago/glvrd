package com.temnenkov.tgibot.tgbot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.temnenkov.tgibot.http.TelegramHttpSender;
import com.temnenkov.tgibot.tgapi.method.SendMessage;
import com.temnenkov.tgibot.tgapi.method.TelegramMethod;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.messaging.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TelegramOutboundChannelAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramOutboundChannelAdapter.class);
    private final Gson gson = new GsonBuilder().create();
    private TelegramHttpSender httpSender;

    public void sendMessage(Message<SendMessage> msg) throws IOException {
      LOGGER.debug("got msg for sending {}", msg);

      SendMessage message = msg.getPayload();

      String request = gson.toJson(message);

        HttpPost httpPost = httpSender.createPost(TelegramMethod.SENDMESSAGE.name(), request);

        try (CloseableHttpResponse response = httpSender.getHttpclient().execute(httpPost)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[Out] {} {}", TelegramMethod.SENDMESSAGE.name(), request);
                LOGGER.debug("[InR] {}", response);
            }
            HttpEntity ht = response.getEntity();
            BufferedHttpEntity buf = new BufferedHttpEntity(ht);
            String responseContent = EntityUtils.toString(buf, StandardCharsets.UTF_8);
            LOGGER.debug("[InC] {}", responseContent);
        }

    }

    @Required
    public void setHttpSender(TelegramHttpSender httpSender) {
        this.httpSender = httpSender;
    }


}
