package com.temnenkov.glvrdbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class HelpCommand extends BotCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelpCommand.class);

    public HelpCommand() {
        super("help", "Краткая справка");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        StringBuilder sb = new StringBuilder();
        sb.append("*Как мной пользоваться?*\n");
        sb.append("Просто пошли мне текст для проверки.\n\n");
        sb.append("Список доступных для меня команд:\n");
        sb.append("/start - самая первая команда.\n");
        sb.append("/help - краткая справка.");

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        answer.setText(sb.toString());
        answer.setParseMode("Markdown");

        try {
            absSender.sendMessage(answer);
        } catch (TelegramApiException e) {
            LOGGER.error("fail execute start command", e);
        }
    }
}