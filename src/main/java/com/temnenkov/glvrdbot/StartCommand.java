package com.temnenkov.glvrdbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class StartCommand extends BotCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartCommand.class);

    public StartCommand() {
        super("start", "Самая первая команда");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        StringBuilder sb = new StringBuilder();
        sb.append("Привет, я бот, использующий сервис \"Главред\" (https://glvrd.ru/)\n");
        sb.append("Для проверки текста просто пошли мне его.");

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        answer.setText(sb.toString());

        try {
            absSender.sendMessage(answer);
        } catch (TelegramApiException e) {
            LOGGER.error("fail execute start command", e);
        }
    }
}