package com.temnenkov.tgibot.tgbot;

import com.temnenkov.glvrd.GlvrdApi;
import com.temnenkov.glvrd.GlvrdResponseHandler;
import com.temnenkov.glvrd.ProofreadResponse;
import com.temnenkov.tgibot.tgapi.dto.Update;
import com.temnenkov.tgibot.tgapi.method.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import java.util.function.Consumer;

public class TelegramMessagesHandler implements BeanFactoryAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramMessagesHandler.class);

    private BeanFactory beanFactory;

    private GlvrdResponseHandler glvrdResponseHandler;

    private MessageChannel outMessages;

    private TelegramCommander telegramCommander;

    public Message<SendMessage> handleUpdates(Message<Update> updateMsg) {

        Update update = updateMsg.getPayload();
        final String text = update.getMessage().getText();


        if (update.getMessage().isCommand()){
            LOGGER.debug("got command {} from {}", text, update);
           return MessageBuilder.withPayload(telegramCommander.handleUpdates(update)).build();
        }


        LOGGER.debug("got msg {} from {}", text, update);

        final GlvrdApi glvrd = createGlvrdApi();

        glvrd.getStatus(ok -> {
            LOGGER.debug("Result {}", ok);
            if (ok) {
                glvrd.proofread(text, new HandleUpdate(update, text));
            } else {

                SendMessage message = SendMessage.builder()
                        .chatId(update.getMessage().getChat().getId())
                        .text("сервис главреда недоступен").build();
                sendMessage(message);
            }
        });


        return MessageBuilder.withPayload(
                SendMessage.builder()
                        .chatId(update.getMessage().getChat().getId())
                        .text("Запрос принят")
                        .build())
                .build();

    }

    private GlvrdApi createGlvrdApi() {
        return beanFactory.getBean(GlvrdApi.class);
    }


    private void sendMessage(SendMessage message) {
        MessagingTemplate template = new MessagingTemplate();
        template.send(outMessages, MessageBuilder.withPayload(message).build());
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Required
    public void setGlvrdResponseHandler(GlvrdResponseHandler glvrdResponseHandler) {
        this.glvrdResponseHandler = glvrdResponseHandler;
    }

    @Required
    public void setOutMessages(MessageChannel outMessages) {
        this.outMessages = outMessages;
    }

    private class HandleUpdate implements Consumer<ProofreadResponse> {
        private final Update update;
        private final String text;

        public HandleUpdate(Update update, String text) {
            this.update = update;
            this.text = text;
        }

        @Override
        public void accept(ProofreadResponse proofreadResponse) {
            LOGGER.debug("proofreadResponse {} for {}", proofreadResponse, update);

            SendMessage message = SendMessage.builder()
                    .chatId(update.getMessage().getChat().getId())
                    .text(glvrdResponseHandler.handle(text, proofreadResponse))
                    .parseMode("HTML").build();
                sendMessage(message); // Call method to send the message


        }
    }

    @Required
    public void setTelegramCommander(TelegramCommander telegramCommander) {
        this.telegramCommander = telegramCommander;
    }

}
