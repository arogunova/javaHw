package ru.hofftech.console;

import ru.hofftech.json.JsonFileService;
import ru.hofftech.model.Parcel;
import ru.hofftech.model.Truck;
import ru.hofftech.service.ParcelService;
import ru.hofftech.service.TruckLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Обработчик команды load (загрузка посылок по именам из репозитория).
 * Поддерживает вывод в текстовом формате или сохранение в JSON.
 */
public class LoadCommands {
    private final ParcelService parcelService;
    private final TruckLoader truckLoader;

    public LoadCommands(ParcelService parcelService, TruckLoader truckLoader) {
        this.parcelService = parcelService;
        this.truckLoader = truckLoader;
    }

    /**
     * Загружает посылки по именам из репозитория и упаковывает их.
     * Ожидает параметры: -parcels-text <имена> -type <алгоритм> -out <формат> [-out-filename <файл>]
     *
     * @param args массив аргументов командной строки
     */
    public void loadParcels(String[] args) {
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
                        ConsolePrinter.printError("после -parcels-text должен быть список посылок");
                        return;
                    }
                    break;
                case "-type":
                    if (i + 1 < args.length) {
                        algorithmType = args[++i];
                    } else {
                        ConsolePrinter.printError("после -type должен быть алгоритм (simple/maxdense)");
                        return;
                    }
                    break;
                case "-out":
                    if (i + 1 < args.length) {
                        outMode = args[++i];
                    } else {
                        ConsolePrinter.printError("после -out должен быть режим (text/json-file)");
                        return;
                    }
                    break;
                case "-out-filename":
                    if (i + 1 < args.length) {
                        outFilename = args[++i];
                    } else {
                        ConsolePrinter.printError("после -out-filename должно быть имя файла");
                        return;
                    }
                    break;
                default:
                    ConsolePrinter.printError("Неизвестный параметр: " + args[i]);
                    return;
            }
        }

        if (parcelsText == null || algorithmType == null || outMode == null) {
            ConsolePrinter.printError("нужно указать -parcels-text, -type и -out");
            return;
        }

        if (outMode.equals("json-file") && outFilename == null) {
            ConsolePrinter.printError("для -out json-file нужно указать -out-filename");
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
                ConsolePrinter.printError("посылка '" + pName + "' не найдена в репозитории");
                return;
            }
        }

        try {
            List<Truck> trucks = truckLoader.loadParcels(parcelsToLoad, algorithmType, 10);

            if (outMode.equals("text")) {
                truckLoader.printTrucks(trucks);
            } else if (outMode.equals("json-file")) {
                JsonFileService.saveToFile(trucks, outFilename);
                ConsolePrinter.printSuccess("Результат сохранён в " + outFilename);
            }
        } catch (Exception e) {
            ConsolePrinter.printError("Ошибка при упаковке: " + e.getMessage());
        }
    }
}