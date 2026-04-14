package ru.hofftech.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Точка входа для запуска Telegram бота.
 * Читает токен и username из файла bot.properties и регистрирует бота.
 */
public class BotRunner {
    private static final Logger log = LoggerFactory.getLogger(BotRunner.class);

    public static void main(String[] args) {
        log.info("BotRunner запущен");

        String botToken;
        String botUsername;

        try (InputStream input = BotRunner.class.getClassLoader().getResourceAsStream("bot.properties")) {
            if (input == null) {
                log.error("Файл bot.properties не найден");
                return;
            }

            Properties props = new Properties();
            props.load(input);
            botToken = props.getProperty("bot.token");
            botUsername = props.getProperty("bot.username");

            log.info("Токен загружен: {}", botToken != null ? "да" : "нет");
            log.info("Username загружен: {}", botUsername != null ? "да" : "нет");

        } catch (IOException e) {
            log.error("Ошибка чтения bot.properties", e);
            return;
        }

        if (botToken == null || botToken.isEmpty()) {
            log.error("Токен не найден");
            return;
        }

        if (botUsername == null || botUsername.isEmpty()) {
            log.error("Username не найден");
            return;
        }

        log.info("Запуск бота: @{}", botUsername);

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TruckBot(botToken, botUsername));
            log.info("Бот успешно запущен!");
        } catch (TelegramApiException e) {
            log.error("Ошибка запуска бота", e);
        }
    }
}