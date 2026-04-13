package ru.hofftech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hofftech.model.Parcel;
import ru.hofftech.file.ParcelFileReader;
import ru.hofftech.service.TruckLoader;
import ru.hofftech.model.Truck;
import ru.hofftech.json.JsonFileService;
import ru.hofftech.repository.ParcelRepository;
import ru.hofftech.service.ParcelService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Главный класс программы.
 * Поддерживает три режима работы:
 * 1. Работа с посылками (CRUD)
 * 2. Погрузка из текстового файла
 * 3. Загрузка из JSON
 */
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // ========== РЕЖИМ 1: РАБОТА С РЕПОЗИТОРИЕМ ==========
        if (args.length > 0 && (args[0].equals("find-all") ||
                args[0].equals("find") ||
                args[0].equals("create") ||
                args[0].equals("delete") ||
                args[0].equals("load"))) {

            ParcelRepository repository = new ParcelRepository();
            ParcelService parcelService = new ParcelService(repository);

            String command = args[0];

            switch (command) {
                case "find-all":
                    List<Parcel> all = parcelService.getAllParcels();
                    System.out.println("Всего посылок: " + all.size());
                    for (Parcel p : all) {
                        System.out.println("  " + p.getName() + " (" + p.getWidth() + "x" + p.getHeight() + ")");
                    }
                    break;

                case "find":
                    if (args.length < 2) {
                        System.err.println("Usage: find <name>");
                        return;
                    }
                    String name = args[1];
                    try {
                        Parcel p = parcelService.getParcelByName(name);
                        System.out.println("Посылка: " + p.getName());
                        System.out.println("Символ: " + p.getSymbol());
                        System.out.println("Размер: " + p.getWidth() + "x" + p.getHeight());
                        System.out.println("Форма:");
                        for (String line : p.getShape()) {
                            System.out.println("  " + line);
                        }
                    } catch (IllegalArgumentException e) {
                        System.err.println("Ошибка: " + e.getMessage());
                    }
                    break;

                case "create":
                    String parcelName = null;
                    String formString = null;

                    for (int i = 1; i < args.length; i++) {
                        switch (args[i]) {
                            case "-name":
                                if (i + 1 < args.length) {
                                    parcelName = args[++i];
                                } else {
                                    System.err.println("Ошибка: после -name должно быть имя");
                                    return;
                                }
                                break;
                            case "-form":
                                if (i + 1 < args.length) {
                                    formString = args[++i];
                                } else {
                                    System.err.println("Ошибка: после -form должна быть форма");
                                    return;
                                }
                                break;
                            default:
                                System.err.println("Неизвестный параметр: " + args[i]);
                                return;
                        }
                    }

                    if (parcelName == null || formString == null) {
                        System.err.println("Ошибка: нужно указать -name и -form");
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
                        System.err.println("Ошибка: не удалось определить символ из формы");
                        return;
                    }

                    try {
                        parcelService.createParcel(parcelName, shape, symbol);
                        System.out.println("✅ Посылка '" + parcelName + "' создана");

                        Parcel created = parcelService.getParcelByName(parcelName);
                        System.out.println("Форма:");
                        for (String line : created.getShape()) {
                            System.out.println("  " + line);
                        }
                    } catch (IllegalArgumentException e) {
                        System.err.println("Ошибка: " + e.getMessage());
                    }
                    break;

                case "delete":
                    if (args.length < 2) {
                        System.err.println("Usage: delete <name>");
                        return;
                    }
                    String deleteName = args[1];
                    try {
                        parcelService.deleteParcel(deleteName);
                        System.out.println("Посылка '" + deleteName + "' удалена");
                    } catch (IllegalArgumentException e) {
                        System.err.println("Ошибка: " + e.getMessage());
                    }
                    break;

                case "load":
                    String parcelsText = null;
                    String algorithmType = null;
                    String outMode = null;
                    String outFilename = null;

                    for (int i = 1; i < args.length; i++) {
                        switch (args[i]) {
                            case "-parcels-text":
                                if (i + 1 < args.length) {
                                    parcelsText = args[++i];
                                } else {
                                    System.err.println("Ошибка: после -parcels-text должен быть список посылок");
                                    return;
                                }
                                break;
                            case "-type":
                                if (i + 1 < args.length) {
                                    algorithmType = args[++i];
                                } else {
                                    System.err.println("Ошибка: после -type должен быть алгоритм (simple/maxdense)");
                                    return;
                                }
                                break;
                            case "-out":
                                if (i + 1 < args.length) {
                                    outMode = args[++i];
                                } else {
                                    System.err.println("Ошибка: после -out должен быть режим (text/json-file)");
                                    return;
                                }
                                break;
                            case "-out-filename":
                                if (i + 1 < args.length) {
                                    outFilename = args[++i];
                                } else {
                                    System.err.println("Ошибка: после -out-filename должно быть имя файла");
                                    return;
                                }
                                break;
                            default:
                                System.err.println("Неизвестный параметр: " + args[i]);
                                return;
                        }
                    }

                    if (parcelsText == null || algorithmType == null || outMode == null) {
                        System.err.println("Ошибка: нужно указать -parcels-text, -type и -out");
                        return;
                    }

                    if (outMode.equals("json-file") && outFilename == null) {
                        System.err.println("Ошибка: для -out json-file нужно указать -out-filename");
                        return;
                    }

                    String[] names = parcelsText.split("\\\\n");
                    List<Parcel> parcelsToLoad = new ArrayList<>();

                    for (String pName : names) {
                        try {
                            Parcel p = parcelService.getParcelByName(pName);
                            parcelsToLoad.add(p);
                            System.out.println("✅ Найдена посылка: " + pName);
                        } catch (IllegalArgumentException e) {
                            System.err.println("❌ Ошибка: посылка '" + pName + "' не найдена в репозитории");
                            return;
                        }
                    }

                    try {
                        TruckLoader truckLoader = new TruckLoader();
                        List<Truck> trucks = truckLoader.loadParcels(parcelsToLoad, algorithmType, 10);

                        if (outMode.equals("text")) {
                            truckLoader.printTrucks(trucks);
                        } else if (outMode.equals("json-file")) {
                            JsonFileService.saveToFile(trucks, outFilename);
                            System.out.println("✅ Результат сохранён в " + outFilename);
                        }

                    } catch (Exception e) {
                        System.err.println("Ошибка при упаковке: " + e.getMessage());
                    }
                    break;
            }
            return;
        }

        // ========== РЕЖИМ 2: ПОГРУЗКА ИЗ ФАЙЛА ==========
        if (args.length == 0) {
            printUsage();
            return;
        }

        // ========== РЕЖИМ 3: ЗАГРУЗКА ИЗ JSON ==========
        if (args[0].equals("--load")) {
            if (args.length < 2) {
                System.err.println("Error: Missing JSON file path");
                return;
            }
            String jsonFile = args[1];
            log.info("Loading trucks from JSON: {}", jsonFile);

            try {
                List<Truck> trucks = JsonFileService.loadFromFile(jsonFile);
                TruckLoader loader = new TruckLoader();
                loader.printTrucks(trucks);
            } catch (Exception e) {
                log.error("Error loading JSON: {}", e.getMessage());
            }
            return;
        }

        // ========== РЕЖИМ 2 (ПРОДОЛЖЕНИЕ): ПОГРУЗКА ИЗ ФАЙЛА ==========
        if (args.length < 3) {
            log.error("Insufficient arguments provided");
            printUsage();
            return;
        }

        String filePath = args[0];
        String algorithm = args[1];
        int maxTrucks;

        try {
            maxTrucks = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            log.error("Invalid max trucks: {}", args[2]);
            printUsage();
            return;
        }

        if (!isValidAlgorithm(algorithm)) {
            log.error("Unknown algorithm: {}", algorithm);
            printUsage();
            return;
        }

        log.info("=== TRUCK LOADER ===");
        log.info("File: {}", filePath);
        log.info("Algorithm: {}", algorithm);
        log.info("Max trucks: {}", maxTrucks);
        log.info("===================");

        ParcelFileReader reader = new ParcelFileReader();

        try {
            List<Parcel> parcels = reader.readFromFile(filePath);
            log.info("✅ File successfully loaded!");
            reader.printParcels(parcels);

            TruckLoader loader = new TruckLoader();

            log.info("=== LOADING WITH {} ALGORITHM ===", algorithm.toUpperCase());
            List<Truck> trucks = loader.loadParcels(parcels, algorithm, maxTrucks);

            loader.printTrucks(trucks);

            if (args.length > 3 && args[3].equals("--save")) {
                if (args.length < 5) {
                    log.error("Missing output file for --save");
                } else {
                    String jsonFile = args[4];
                    log.info("Saving result to JSON: {}", jsonFile);
                    JsonFileService.saveToFile(trucks, jsonFile);
                    System.out.println("✅ Saved to " + jsonFile);
                }
            }

        } catch (IOException e) {
            log.error("Error reading file: {}", e.getMessage());
            log.error("Check path: {}", filePath);
        } catch (IllegalArgumentException e) {
            log.error("Error: {}", e.getMessage());
        }
    }

    private static boolean isValidAlgorithm(String algorithm) {
        return algorithm.equalsIgnoreCase("simple") ||
                algorithm.equalsIgnoreCase("maxdense");
    }

    private static void printUsage() {
        System.out.println("=== TRUCK LOADER USAGE ===");
        System.out.println();
        System.out.println("=== РЕЖИМ 1: Работа с посылками (CRUD) ===");
        System.out.println("  find-all");
        System.out.println("       Пример: find-all");
        System.out.println();
        System.out.println("  find <name>");
        System.out.println("       Пример: find Посылка_тип_1");
        System.out.println();
        System.out.println("  create -name <name> -form <form>");
        System.out.println("       Пример: create -name \"Куб\" -form \"XXX\\nXXX\\nXXX\"");
        System.out.println();
        System.out.println("  delete <name>");
        System.out.println("       Пример: delete Куб");
        System.out.println();
        System.out.println("  load -parcels-text \"<name1>\\n<name2>\" -type <algorithm> -out <text|json-file> [-out-filename <file>]");
        System.out.println("       Пример (текст): load -parcels-text \"Посылка_тип_1\\nКуб\" -type maxdense -out text");
        System.out.println("       Пример (JSON):  load -parcels-text \"Посылка_тип_1\\nКуб\" -type maxdense -out json-file -out-filename result.json");
        System.out.println();
        System.out.println("=== РЕЖИМ 2: Погрузка из текстового файла ===");
        System.out.println("  java ru.hofftech.Main <file-path> <algorithm> <max-trucks> [--save <json-file>]");
        System.out.println("       Пример: java ru.hofftech.Main test.txt maxdense 3");
        System.out.println("       Пример с сохранением: java ru.hofftech.Main test.txt maxdense 3 --save result.json");
        System.out.println();
        System.out.println("=== РЕЖИМ 3: Загрузка из JSON ===");
        System.out.println("  java ru.hofftech.Main --load <json-file>");
        System.out.println("       Пример: java ru.hofftech.Main --load result.json");
        System.out.println();
        System.out.println("=== ЗАПУСК TELEGRAM БОТА ===");
        System.out.println("  java ru.hofftech.telegram.BotRunner");
        System.out.println("       (требуется файл bot.properties в resources с bot.token и bot.username)");
        System.out.println("=========================");
    }
}