package ru.hofftech.console;

import ru.hofftech.model.Parcel;
import ru.hofftech.service.ParcelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Обработчик команды создания посылки.
 * Поддерживает команду: create.
 */
public class CreateCommands {
    private final ParcelService parcelService;

    public CreateCommands(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    /**
     * Создаёт новую посылку из параметров командной строки.
     * Ожидает параметры: -name <имя> -form <форма>
     *
     * @param args массив аргументов командной строки
     */
    public void create(String[] args) {
        String parcelName = null;
        String formString = null;

        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "-name":
                    if (i + 1 < args.length) {
                        parcelName = args[++i];
                    } else {
                        ConsolePrinter.printError("после -name должно быть имя");
                        return;
                    }
                    break;
                case "-form":
                    if (i + 1 < args.length) {
                        formString = args[++i];
                    } else {
                        ConsolePrinter.printError("после -form должна быть форма");
                        return;
                    }
                    break;
                default:
                    ConsolePrinter.printError("Неизвестный параметр: " + args[i]);
                    return;
            }
        }

        if (parcelName == null || formString == null) {
            ConsolePrinter.printError("нужно указать -name и -form");
            return;
        }

        String[] formLines = formString.split("\\\\n");
        List<String> shape = new ArrayList<>(Arrays.asList(formLines));

        char symbol = '?';
        for (String line : shape) {
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (!Character.isWhitespace(c)) {
                    symbol = c;
                    break;
                }
            }
            if (symbol != '?') break;
        }

        if (symbol == '?') {
            ConsolePrinter.printError("не удалось определить символ из формы");
            return;
        }

        try {
            parcelService.createParcel(parcelName, shape, symbol);
            ConsolePrinter.printSuccess("Посылка '" + parcelName + "' создана");

            Parcel created = parcelService.getParcelByName(parcelName);
            System.out.println("Форма:");
            for (String line : created.getShape()) {
                System.out.println("  " + line);
            }
        } catch (IllegalArgumentException e) {
            ConsolePrinter.printError(e.getMessage());
        }
    }
}