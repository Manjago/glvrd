package com.temnenkov.tgibot.tgbot;

import com.beust.jcommander.internal.Lists;
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

import java.util.List;
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


        if (update.getMessage().isCommand()) {
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
                List<String> result = splitInChunks(modifiedText, MAX_MESSAGE_SIZE);
                for (String s : result) {
                    SendMessage message = SendMessage.builder()
                            .chatId(update.getMessage().getChat().getId())
                            .text(s)
                            .parseMode("HTML").build();
                    sendMessage(message);
                }

            }


        }


        private List<String> splitInChunks(String s, int chunkSize) {
            List<String> result = Lists.newArrayList();
            int length = s.length();
            for (int i = 0; i < length; i += chunkSize) {
                result.add(s.substring(i, Math.min(length, i + chunkSize)));
            }
            return result;
        }
    }

}
