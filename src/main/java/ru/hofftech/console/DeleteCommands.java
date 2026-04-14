package ru.hofftech.console;

import ru.hofftech.service.ParcelService;

/**
 * Обработчик команды удаления посылки.
 * Поддерживает команду: delete.
 */
public class DeleteCommands {
    private final ParcelService parcelService;

    public DeleteCommands(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    /**
     * Удаляет посылку по имени.
     *
     * @param name имя посылки для удаления
     */
    public void delete(String name) {
        try {
            parcelService.deleteParcel(name);
            ConsolePrinter.printSuccess("Посылка '" + name + "' удалена");
        } catch (IllegalArgumentException e) {
            ConsolePrinter.printError(e.getMessage());
        }
    }
}