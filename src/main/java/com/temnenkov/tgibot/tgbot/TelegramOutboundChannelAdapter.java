package com.temnenkov.tgibot.tgbot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

public class TelegramOutboundChannelAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramOutboundChannelAdapter.class);
    private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    private TelegramHttpSender httpSender;

    private MessageChannel outMessages;

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

            JsonObject jsonObject = gson.fromJson(responseContent, JsonObject.class);
            if (!jsonObject.get("ok").getAsBoolean()) {
                LOGGER.error("Error sending request = {}, response = {}, responseContent = {}", request, response, responseContent);

                JsonElement description = jsonObject.get("description");
                if (description != null && description.getAsString() != null && description.getAsString().contains("can't parse entities")) {
                   sendTooLongErrorMessage(message, responseContent);
                } else {
                    sendErrorMessage(message, responseContent);
                }
            }
        }

    }

    private void sendErrorMessage(SendMessage message, String errResponse) {
        coreSendErrorMessage(message, errResponse, "Ошибка, сообщите разработчику (https://t.me/glvrd2): {0}");
    }

    private void sendTooLongErrorMessage(SendMessage message, String errResponse) {
        coreSendErrorMessage(message, errResponse, "Слишком длинный текст с форматированием (жирный шрифт, курсив и т.п.). Попробуйте разбить текст на несколько частей или избавиться от форматирования. Если даже после этого ошибка будет повторяться - сообщите разработчику (https://t.me/glvrd2): {0}");
    }

    private void coreSendErrorMessage(SendMessage message, String errResponse, String userText) {

        if (errResponse != null && errResponse.contains("bot was blocked")) {
            //blocked? ok
            return;
        }

        message.setParseMode(null);
        message.setText(MessageFormat.format(userText, errResponse));

        MessagingTemplate template = new MessagingTemplate();
        template.send(outMessages, MessageBuilder.withPayload(message).build());
    }

    @Required
    public void setHttpSender(TelegramHttpSender httpSender) {
        this.httpSender = httpSender;
    }

    @Required
    public void setOutMessages(MessageChannel outMessages) {
        this.outMessages = outMessages;
    }

}
