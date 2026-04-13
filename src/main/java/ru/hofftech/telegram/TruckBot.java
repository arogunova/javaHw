package ru.hofftech.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.hofftech.model.Parcel;
import ru.hofftech.model.Truck;
import ru.hofftech.repository.ParcelRepository;
import ru.hofftech.service.ParcelService;
import ru.hofftech.service.TruckLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("deprecation")
public class TruckBot extends TelegramLongPollingBot {
    private static final Logger log = LoggerFactory.getLogger(TruckBot.class);

    private final ParcelService parcelService;
    private final String botUsername;
    private final String botToken;

    public TruckBot(String botToken, String botUsername) {
        this.botToken = botToken;
        this.botUsername = botUsername;
        ParcelRepository repository = new ParcelRepository();
        this.parcelService = new ParcelService(repository);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            log.info("Received message: {} from {}", messageText, chatId);

            String response = processCommand(messageText);
            sendMessage(chatId, response);
        }
    }

    private String processCommand(String command) {
        String[] parts = command.split(" ");
        String cmd = parts[0];

        try {
            return switch (cmd) {
                case "/find-all" -> getAllParcelsResponse();
                case "/find" -> {
                    if (parts.length < 2) yield "Usage: /find <name>";
                    yield getParcelResponse(parts[1]);
                }
                case "/create" -> createParcelResponse(command);
                case "/delete" -> {
                    if (parts.length < 2) yield "Usage: /delete <name>";
                    yield deleteParcelResponse(parts[1]);
                }
                case "/load" -> loadParcelsResponse(command);
                default -> "Неизвестная команда. Доступные: /find-all, /find, /create, /delete, /load";
            };
        } catch (Exception e) {
            log.error("Error processing command", e);
            return "Ошибка: " + e.getMessage();
        }
    }

    private String getAllParcelsResponse() {
        List<Parcel> all = parcelService.getAllParcels();
        if (all.isEmpty()) {
            return "Нет посылок в репозитории";
        }
        StringBuilder sb = new StringBuilder("Всего посылок: " + all.size() + "\n");
        for (Parcel p : all) {
            sb.append("• ").append(p.getName()).append(" (").append(p.getWidth()).append("x").append(p.getHeight()).append(")\n");
        }
        return sb.toString();
    }

    private String getParcelResponse(String name) {
        try {
            Parcel p = parcelService.getParcelByName(name);
            StringBuilder sb = new StringBuilder();
            sb.append("📦 Посылка: ").append(p.getName()).append("\n");
            sb.append("Символ: ").append(p.getSymbol()).append("\n");
            sb.append("Размер: ").append(p.getWidth()).append("x").append(p.getHeight()).append("\n");
            sb.append("Форма:\n");
            for (String line : p.getShape()) {
                sb.append("  ").append(line).append("\n");
            }
            return sb.toString();
        } catch (IllegalArgumentException e) {
            return "❌ Ошибка: " + e.getMessage();
        }
    }

    private String deleteParcelResponse(String name) {
        try {
            parcelService.deleteParcel(name);
            return "✅ Посылка '" + name + "' удалена";
        } catch (IllegalArgumentException e) {
            return "❌ Ошибка: " + e.getMessage();
        }
    }

    private String createParcelResponse(String command) {
        String paramsPart = command.substring(8).trim();

        String name = null;
        String formString = null;

        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < paramsPart.length(); i++) {
            char c = paramsPart.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
                if (!inQuotes && !current.isEmpty()) {
                    tokens.add(current.toString());
                    current = new StringBuilder();
                }
            } else if (c == ' ' && !inQuotes) {
                if (!current.isEmpty()) {
                    tokens.add(current.toString());
                    current = new StringBuilder();
                }
            } else {
                current.append(c);
            }
        }
        if (!current.isEmpty()) {
            tokens.add(current.toString());
        }

        for (int i = 0; i < tokens.size(); i++) {
            switch (tokens.get(i)) {
                case "-name":
                    if (i + 1 < tokens.size()) {
                        name = tokens.get(++i);
                    }
                    break;
                case "-form":
                    if (i + 1 < tokens.size()) {
                        formString = tokens.get(++i);
                    }
                    break;
            }
        }

        if (name == null || formString == null) {
            return "Ошибка: нужно указать -name и -form";
        }

        String[] formLines = formString.split("\\\\n");
        List<String> shape = new ArrayList<>(Arrays.asList(formLines));

        // Берём символ из формы — первый непробельный символ
        char symbol = '?';
        for (String line : shape) {
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (!Character.isWhitespace(c)) {
                    symbol = c;
                    break;
                }
            }
            if (symbol != '?') break;
        }

        if (symbol == '?') {
            return "Ошибка: не удалось определить символ из формы";
        }

        try {
            parcelService.createParcel(name, shape, symbol);
            StringBuilder sb = new StringBuilder();
            sb.append("✅ Посылка '").append(name).append("' создана!\n");
            sb.append("Форма:\n");
            for (String line : shape) {
                sb.append("  ").append(line).append("\n");
            }
            return sb.toString();
        } catch (IllegalArgumentException e) {
            return "❌ Ошибка: " + e.getMessage();
        }
    }

    private String loadParcelsResponse(String command) {
        String paramsPart = command.substring(6).trim();

        String parcelsText = null;
        String algorithmType = null;

        String[] parts = paramsPart.split(" ");
        for (int i = 0; i < parts.length; i++) {
            switch (parts[i]) {
                case "-parcels":
                    if (i + 1 < parts.length) {
                        String raw = parts[++i];
                        if (raw.startsWith("\"") && raw.endsWith("\"")) {
                            parcelsText = raw.substring(1, raw.length() - 1);
                        } else {
                            parcelsText = raw;
                        }
                    } else {
                        return "Ошибка: после -parcels должен быть список посылок";
                    }
                    break;
                case "-type":
                    if (i + 1 < parts.length) {
                        algorithmType = parts[++i];
                    } else {
                        return "Ошибка: после -type должен быть алгоритм (simple/maxdense)";
                    }
                    break;
            }
        }

        if (parcelsText == null || algorithmType == null) {
            return "Ошибка: нужно указать -parcels и -type";
        }

        if (!algorithmType.equals("simple") && !algorithmType.equals("maxdense")) {
            return "Ошибка: алгоритм должен быть simple или maxdense";
        }

        String[] names = parcelsText.split("\\\\n");
        List<Parcel> parcelsToLoad = new ArrayList<>();

        for (String name : names) {
            String cleanName = name.trim();
            if (cleanName.startsWith("\"") && cleanName.endsWith("\"")) {
                cleanName = cleanName.substring(1, cleanName.length() - 1);
            }

            try {
                Parcel p = parcelService.getParcelByName(cleanName);
                parcelsToLoad.add(p);
            } catch (IllegalArgumentException e) {
                return "❌ Ошибка: посылка '" + cleanName + "' не найдена в репозитории";
            }
        }

        try {
            TruckLoader truckLoader = new TruckLoader();
            List<Truck> trucks = truckLoader.loadParcels(parcelsToLoad, algorithmType, 10);

            StringBuilder sb = new StringBuilder();
            sb.append("📦 Результат упаковки:\n");
            sb.append("Использовано машин: ").append(trucks.size()).append("\n\n");

            for (int i = 0; i < trucks.size(); i++) {
                sb.append("Машина ").append(i + 1).append(":\n");
                sb.append(trucks.get(i).toString()).append("\n");
            }

            return sb.toString();
        } catch (Exception e) {
            return "❌ Ошибка при упаковке: " + e.getMessage();
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending message", e);
        }
    }
}