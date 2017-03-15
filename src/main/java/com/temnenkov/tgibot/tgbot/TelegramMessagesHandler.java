package com.temnenkov.tgibot.tgbot;

import com.temnenkov.glvrd.*;
import com.temnenkov.tgibot.tgapi.dto.Update;
import com.temnenkov.tgibot.tgapi.method.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import java.util.List;
import java.util.function.Consumer;

public class TelegramMessagesHandler implements BeanFactoryAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramMessagesHandler.class);

    private BeanFactory beanFactory;

    private GlvrdResponseHandler glvrdResponseHandler;

    private MessageChannel outMessages;

    private TelegramCommander telegramCommander;

    private TextSplitter textSplitter;

    public Message<SendMessage> handleUpdates(Message<Update> updateMsg) {

        Update update = updateMsg.getPayload();
        final String text = update.getMessage().getText();


        if (update.getMessage().isCommand()) {
            LOGGER.debug("got command {} from {}", text, update);
            return MessageBuilder.withPayload(telegramCommander.handleUpdates(update)).build();
        }


        LOGGER.debug("got msg {} from {}", text, update);

        final GlvrdApi glvrd = createGlvrdApi();

        try{
            glvrd.getStatus(ok -> {
                LOGGER.debug("Result {}", ok);
                if (ok) {
                    callProofRead(update, text, glvrd);
                } else {
                    sendErrorMessage(update);
                }
            });

        } catch (GlvrdException ex) {
            LOGGER.error("fail call getStatus", ex);
            return MessageBuilder.withPayload(
                    SendMessage.builder()
                            .chatId(update.getMessage().getChat().getId())
                            .text("сервис главреда недоступен, попробуйте повторить запрос через час")
                            .build())
                    .build();
        }


        return MessageBuilder.withPayload(
                SendMessage.builder()
                        .chatId(update.getMessage().getChat().getId())
                        .text("Запрос принят")
                        .build())
                .build();

    }

    private void callProofRead(Update update, String text, GlvrdApi glvrd) {
        try {
            glvrd.proofread(text, new HandleUpdate(update, text));
        } catch (GlvrdException ex) {
            LOGGER.error("fail call proofread for update {} and text {}", update, text, ex);
            sendErrorMessage(update);
        }
    }

    private void sendErrorMessage(Update update) {
        SendMessage message = SendMessage.builder()
                .chatId(update.getMessage().getChat().getId())
                .text("сервис главреда недоступен, попробуйте повторить запрос через час").build();
        sendMessage(message);
    }

    private GlvrdApi createGlvrdApi() {
        return beanFactory.getBean(GlvrdApi.class);
    }


    private void sendMessage(SendMessage message) {
        MessagingTemplate template = new MessagingTemplate();
        template.send(outMessages, MessageBuilder.withPayload(message).build());
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
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

    @Required
    public void setTelegramCommander(TelegramCommander telegramCommander) {
        this.telegramCommander = telegramCommander;
    }

    private class HandleUpdate implements Consumer<ProofreadResponse> {
        private static final int MAX_MESSAGE_SIZE = 4096;
        private final Update update;
        private final String text;

        public HandleUpdate(Update update, String text) {
            this.update = update;
            this.text = text;
        }

        @Override
        public void accept(ProofreadResponse proofreadResponse) {
            LOGGER.debug("proofreadResponse {} for {}", proofreadResponse, update);


            final String modifiedText = glvrdResponseHandler.handle(text, proofreadResponse);
            if (modifiedText.length() <= MAX_MESSAGE_SIZE) {
                SendMessage message = SendMessage.builder()
                        .chatId(update.getMessage().getChat().getId())
                        .text(modifiedText)
                        .parseMode("HTML").build();
                sendMessage(message);
            } else {
                List<String> result = textSplitter.splitInChunks(modifiedText, MAX_MESSAGE_SIZE);
                for (String s : result) {
                    SendMessage message = SendMessage.builder()
                            .chatId(update.getMessage().getChat().getId())
                            .text(s)
                            .parseMode("HTML").build();
                    sendMessage(message);
                }

            }


        }

    }

    @Required
    public void setTextSplitter(TextSplitter textSplitter) {
        this.textSplitter = textSplitter;
    }


}
