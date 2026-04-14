package ru.hofftech;

import ru.hofftech.console.*;
import ru.hofftech.repository.ParcelRepository;
import ru.hofftech.service.ParcelService;
import ru.hofftech.service.TruckLoader;

/**
 * Главный класс программы.
 * Точка входа. Выполняет маршрутизацию команд на соответствующие обработчики.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            ConsolePrinter.printUsage();
            return;
        }

        ParcelRepository repository = new ParcelRepository();
        ParcelService parcelService = new ParcelService(repository);
        TruckLoader truckLoader = new TruckLoader();

        FindCommands findCommands = new FindCommands(parcelService);
        CreateCommands createCommands = new CreateCommands(parcelService);
        DeleteCommands deleteCommands = new DeleteCommands(parcelService);
        LoadCommands loadCommands = new LoadCommands(parcelService, truckLoader);
        FileLoadCommands fileLoadCommands = new FileLoadCommands(truckLoader);
        JsonCommands jsonCommands = new JsonCommands(truckLoader);

        String command = args[0];

        switch (command) {
            case "find-all":
                findCommands.findAll();
                break;

            case "find":
                if (args.length < 2) {
                    ConsolePrinter.printError("Usage: find <name>");
                    return;
                }
                findCommands.find(args[1]);
                break;

            case "create":
                createCommands.create(args);
                break;

            case "delete":
                if (args.length < 2) {
                    ConsolePrinter.printError("Usage: delete <name>");
                    return;
                }
                deleteCommands.delete(args[1]);
                break;

            case "load":
                loadCommands.loadParcels(args);
                break;

            case "--load":
                if (args.length < 2) {
                    ConsolePrinter.printError("Usage: --load <json-file>");
                    return;
                }
                jsonCommands.loadFromJson(args[1]);
                break;

            default:
                fileLoadCommands.loadFromFile(args);
                break;
        }
    }
}