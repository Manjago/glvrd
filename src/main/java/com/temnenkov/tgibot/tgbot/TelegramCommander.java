package com.temnenkov.tgibot.tgbot;

import com.temnenkov.tgibot.tgapi.dto.Update;
import com.temnenkov.tgibot.tgapi.method.SendMessage;

import java.text.MessageFormat;

public class TelegramCommander {

    public SendMessage handleUpdates(Update update) {

        String text = update.getMessage().getText();

        switch (text) {
            case "/start":
                return startCommand(update);
            case "/help":
                return helpCommand(update);
            default:
                return unknownCommand(update);

        }

    }

    private SendMessage startCommand(Update update) {
        StringBuilder sb = new StringBuilder();
        sb.append("Привет, я бот, использующий сервис \"Главред\" (https://glvrd.ru/)\n");
        sb.append("Для проверки текста просто пошли мне его.");

        return SendMessage.builder()
                .chatId(update.getMessage().getChat().getId())
                .text(sb.toString()).build();
    }

    private SendMessage helpCommand(Update update) {

        return SendMessage.builder()
                .chatId(update.getMessage().getChat().getId())
                .text(helpMessage())
                .parseMode("Markdown")
                .build();
    }

    private String helpMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("*Как мной пользоваться?*\n");
        sb.append("Просто пошли мне текст для проверки.\n\n");
        sb.append("Список доступных для меня команд:\n");
        sb.append("/start - самая первая команда.\n");
        sb.append("/help - краткая справка.");
        return sb.toString();
    }

    private SendMessage unknownCommand(Update update) {

        StringBuilder sb = new StringBuilder();
        sb.append(MessageFormat.format("Неизвестная команда {0}", update.getMessage().getText()));
        sb.append("\n\n");
        sb.append(helpMessage());

        return SendMessage.builder()
                .chatId(update.getMessage().getChat().getId())
                .text(sb.toString())
                .build();
    }
}