package ru.hofftech.console;

import ru.hofftech.json.JsonFileService;
import ru.hofftech.model.Truck;
import ru.hofftech.service.TruckLoader;

import java.util.List;

/**
 * Обработчик команды загрузки из JSON файла.
 * Восстанавливает машины из JSON и выводит их на экран.
 */
public class JsonCommands {
    private final TruckLoader truckLoader;

    public JsonCommands(TruckLoader truckLoader) {
        this.truckLoader = truckLoader;
    }

    /**
     * Загружает машины из JSON файла и выводит их на экран.
     *
     * @param jsonFile путь к JSON файлу
     */
    public void loadFromJson(String jsonFile) {
        System.out.println("Loading trucks from JSON: " + jsonFile);

        try {
            List<Truck> trucks = JsonFileService.loadFromFile(jsonFile);
            truckLoader.printTrucks(trucks);
        } catch (Exception e) {
            ConsolePrinter.printError("Ошибка загрузки JSON: " + e.getMessage());
        }
    }
}