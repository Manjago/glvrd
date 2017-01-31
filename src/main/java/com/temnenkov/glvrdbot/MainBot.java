package com.temnenkov.glvrdbot;

import com.temnenkov.glvrd.GlvrdApi;
import com.temnenkov.glvrd.ProofreadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.function.Consumer;

public class MainBot extends TelegramLongPollingCommandBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainBot.class);
    private final GlvrdResponseHandler glvrdResponseHandler = new GlvrdResponseHandler();

    private final String botUsername;
    private final String botToken;

    public MainBot(String botUsername, String botToken) {
        this.botToken = botToken;
        this.botUsername = botUsername;

        register(new StartCommand());
        final HelpCommand helpCommand = new HelpCommand();
        register(helpCommand);

        registerDefaultAction((absSender, message) -> {
            SendMessage commandUnknownMessage = new SendMessage();
            commandUnknownMessage.setChatId(message.getChatId());
            commandUnknownMessage.setText(MessageFormat.format("Неизвестная команда {0}", message.getText()));
            try {
                absSender.sendMessage(commandUnknownMessage);
            } catch (TelegramApiException e) {
                LOGGER.error("fail send text about unknown command", e);
            }
            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[] {});
        });

    }

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException {

        if (args.length < 1){
            LOGGER.error("no config file");
            return;
        }

        Properties props;
        try {
            props = loadProperties(args[0]);
        } catch (IOException e) {
            LOGGER.error("can not read config file {}", args[0], e);
            return;
        }


        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(new MainBot(props.getProperty("botUserName"), props.getProperty("botToken")));
        } catch (TelegramApiException e) {
            LOGGER.error("fail register bot", e);
        }

    }

    private static Properties loadProperties(String filepath) throws IOException {
        try (InputStream propFile = new FileInputStream(filepath)) {
            Properties p =
                    new Properties(System.getProperties());
            p.load(propFile);
            return p;
        }
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            final String text = update.getMessage().getText();
            LOGGER.debug("got msg {} from {}", text, update);

            final GlvrdApi glvrd = new GlvrdApi();

            glvrd.getStatus(ok -> {
                LOGGER.debug("Result {}", ok);
                if (ok) {
                    glvrd.proofread(text, new HandleUpdate(update, text));
                } else {

                    SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                            .setChatId(update.getMessage().getChatId())
                            .setText("сервис главреда недоступен");
                    try {
                        sendMessage(message); // Call method to send the message
                    } catch (TelegramApiException e) {
                        LOGGER.error("fail send message {} for {}", message, update, e);
                    }
                }
            });


        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
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

            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(update.getMessage().getChatId())
                    .setText(glvrdResponseHandler.handle(text, proofreadResponse))
                    .setParseMode("HTML");
            try {
                sendMessage(message); // Call method to send the message
            } catch (TelegramApiException e) {
                LOGGER.error("fail send message {} for {}", message, update, e);
            }


        }
    }
}
