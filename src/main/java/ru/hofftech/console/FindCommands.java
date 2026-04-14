package ru.hofftech.console;

import ru.hofftech.model.Parcel;
import ru.hofftech.service.ParcelService;

import java.util.List;

/**
 * Обработчик команд поиска и просмотра посылок.
 * Поддерживает команды: find-all, find.
 */
public class FindCommands {
    private final ParcelService parcelService;

    public FindCommands(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    /**
     * Выводит список всех посылок в репозитории.
     */
    public void findAll() {
        List<Parcel> all = parcelService.getAllParcels();
        System.out.println("Всего посылок: " + all.size());
        for (Parcel p : all) {
            System.out.println("  " + p.getName() + " (" + p.getWidth() + "x" + p.getHeight() + ")");
        }
    }

    /**
     * Находит и выводит информацию о посылке по имени.
     *
     * @param name имя посылки
     */
    public void find(String name) {
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
            ConsolePrinter.printError(e.getMessage());
        }
    }
}