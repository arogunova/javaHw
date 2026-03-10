package ru.hofftech;

import ru.hofftech.model.Parcel;
import ru.hofftech.file.ParcelFileReader;
import ru.hofftech.service.TruckLoader;
import ru.hofftech.model.Truck;

import java.io.IOException;
import java.util.List;

/**
 * Главный класс программы.
 * Теперь программа:
 * 1. Читает посылки из файла
 * 2. Загружает их в машины
 * 3. Показывает результат
 */
public class Main {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\7101595\\Desktop\\java\\test.txt";

        // Шаг 1: Читаем посылки из файла
        ParcelFileReader reader = new ParcelFileReader();

        try {
            List<Parcel> parcels = reader.readFromFile(filePath);
            System.out.println("\nFile successfully loaded!");
            reader.printParcels(parcels);

            // Шаг 2: Загружаем посылки в машины
            TruckLoader loader = new TruckLoader();

            // Пробуем оба алгоритма
            System.out.println("\n\n=== TESTING SIMPLE ALGORITHM ===");
            List<Truck> simpleTrucks = loader.loadParcels(parcels, false);
            loader.printTrucks(simpleTrucks);

            System.out.println("\n\n=== TESTING OPTIMAL ALGORITHM ===");
            List<Truck> optimalTrucks = loader.loadParcels(parcels, true);
            loader.printTrucks(optimalTrucks);

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.err.println("Check path: " + filePath);
        }
    }
}