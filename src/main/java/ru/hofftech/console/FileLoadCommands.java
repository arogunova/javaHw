package ru.hofftech.console;

import ru.hofftech.file.ParcelFileReader;
import ru.hofftech.json.JsonFileService;
import ru.hofftech.model.Parcel;
import ru.hofftech.model.Truck;
import ru.hofftech.service.TruckLoader;

import java.io.IOException;
import java.util.List;

/**
 * Обработчик команды погрузки из текстового файла (старый режим из ДЗ1/ДЗ2).
 * Поддерживает сохранение результата в JSON.
 */
public class FileLoadCommands {
    private final TruckLoader truckLoader;

    public FileLoadCommands(TruckLoader truckLoader) {
        this.truckLoader = truckLoader;
    }

    /**
     * Загружает посылки из текстового файла, упаковывает и выводит результат.
     * Ожидает аргументы: <file-path> <algorithm> <max-trucks> [--save <json-file>]
     *
     * @param args массив аргументов командной строки
     */
    public void loadFromFile(String[] args) {
        if (args.length < 3) {
            ConsolePrinter.printError("Недостаточно аргументов для погрузки из файла");
            ConsolePrinter.printUsage();
            return;
        }

        String filePath = args[0];
        String algorithm = args[1];
        int maxTrucks;

        try {
            maxTrucks = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            ConsolePrinter.printError("max-trucks должно быть числом: " + args[2]);
            ConsolePrinter.printUsage();
            return;
        }

        if (!isValidAlgorithm(algorithm)) {
            ConsolePrinter.printError("Неизвестный алгоритм: " + algorithm);
            ConsolePrinter.printUsage();
            return;
        }

        System.out.println("=== TRUCK LOADER ===");
        System.out.println("File: " + filePath);
        System.out.println("Algorithm: " + algorithm);
        System.out.println("Max trucks: " + maxTrucks);
        System.out.println("===================\n");

        ParcelFileReader reader = new ParcelFileReader();

        try {
            List<Parcel> parcels = reader.readFromFile(filePath);
            System.out.println("✅ File successfully loaded!");
            reader.printParcels(parcels);

            System.out.println("\n=== LOADING WITH " + algorithm.toUpperCase() + " ALGORITHM ===");
            List<Truck> trucks = truckLoader.loadParcels(parcels, algorithm, maxTrucks);

            truckLoader.printTrucks(trucks);

            // Сохранение в JSON
            if (args.length > 3 && args[3].equals("--save")) {
                if (args.length < 5) {
                    ConsolePrinter.printError("Не указан файл для --save");
                } else {
                    String jsonFile = args[4];
                    JsonFileService.saveToFile(trucks, jsonFile);
                    ConsolePrinter.printSuccess("Saved to " + jsonFile);
                }
            }

        } catch (IOException e) {
            ConsolePrinter.printError("Ошибка чтения файла: " + e.getMessage());
            System.err.println("Check path: " + filePath);
        } catch (IllegalArgumentException e) {
            ConsolePrinter.printError(e.getMessage());
        } catch (Exception e) {
            ConsolePrinter.printError("Ошибка при упаковке: " + e.getMessage());
        }
    }

    /**
     * Проверяет, является ли алгоритм допустимым.
     *
     * @param algorithm название алгоритма
     * @return true если алгоритм simple или maxdense
     */
    private boolean isValidAlgorithm(String algorithm) {
        return algorithm.equalsIgnoreCase("simple") ||
                algorithm.equalsIgnoreCase("maxdense");
    }
}