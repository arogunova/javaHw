package ru.hofftech.service;

import ru.hofftech.model.Parcel;
import ru.hofftech.model.Truck;

import java.util.List;

/**
 * Фасад для загрузки посылок.
 * Выбирает нужную стратегию и делегирует ей работу.
 */
public class TruckLoader {

    /**
     * Загружает посылки с указанной стратегией.
     *
     * @param parcels список посылок
     * @param algorithm название алгоритма ("simple" или "maxdense")
     * @return список загруженных машин
     * @throws IllegalArgumentException если алгоритм не найден
     */
    public List<Truck> loadParcels(List<Parcel> parcels, String algorithm) {
        LoadingStrategy strategy = createStrategy(algorithm);
        return strategy.load(parcels);
    }

    /**
     * Фабричный метод - создает нужную стратегию по имени.
     */
    private LoadingStrategy createStrategy(String algorithm) {
        return switch (algorithm.toLowerCase()) {
            case "simple" -> new LoadingStrategySimple();
            case "maxdense" -> new LoadingStrategyMaxDense();
            default -> throw new IllegalArgumentException(
                    "Unknown algorithm: " + algorithm + ". Available: simple, maxdense"
            );
        };
    }

    /**
     * Печатает результат загрузки.
     */
    public void printTrucks(List<Truck> trucks) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("LOADING RESULT");
        System.out.println("=".repeat(50));

        if (trucks == null || trucks.isEmpty()) {
            System.out.println("No trucks used");
            return;
        }

        System.out.println("Total trucks used: " + trucks.size());

        for (int i = 0; i < trucks.size(); i++) {
            System.out.println("\nTRUCK #" + (i + 1));
            System.out.println(trucks.get(i));
        }

        printStatistics(trucks);
    }

    /**
     * Считает статистику.
     */
    private void printStatistics(List<Truck> trucks) {
        System.out.println("\n" + "-".repeat(30));
        System.out.println("STATISTICS");
        System.out.println("-".repeat(30));

        int totalParcels = 0;
        for (Truck truck : trucks) {
            totalParcels += truck.getPackagesCount();
        }

        System.out.println("Total trucks: " + trucks.size());
        System.out.println("Total parcels: " + totalParcels);
    }
}