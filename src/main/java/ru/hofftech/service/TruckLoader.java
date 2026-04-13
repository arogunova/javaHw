package ru.hofftech.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hofftech.model.Parcel;
import ru.hofftech.model.Truck;

import java.util.List;

/**
 * Фасад для загрузки посылок.
 * Выбирает нужную стратегию в зависимости от переданного алгоритма.
 */
public class TruckLoader {
    private static final Logger log = LoggerFactory.getLogger(TruckLoader.class);

    /**
     * Загружает посылки с указанием максимального количества машин.
     *
     * @param parcels список посылок
     * @param algorithm название алгоритма (simple или maxdense)
     * @param maxTrucks максимальное количество машин
     * @return список загруженных машин
     * @throws LoadingException если загрузка невозможна
     */
    public List<Truck> loadParcels(List<Parcel> parcels, String algorithm, int maxTrucks) {
        log.info("Loading parcels with algorithm: {}", algorithm);
        LoadingStrategy strategy = createStrategy(algorithm);
        return strategy.load(parcels, maxTrucks);
    }

    /**
     * Загружает посылки с максимальным количеством машин по умолчанию (1).
     *
     * @param parcels список посылок
     * @param algorithm название алгоритма
     * @return список загруженных машин
     * @throws LoadingException если загрузка невозможна
     */
    public List<Truck> loadParcels(List<Parcel> parcels, String algorithm) {
        return loadParcels(parcels, algorithm, 1);
    }

    /**
     * Создаёт стратегию загрузки по имени алгоритма.
     *
     * @param algorithm имя алгоритма
     * @return стратегия загрузки
     * @throws IllegalArgumentException если алгоритм не найден
     */
    private LoadingStrategy createStrategy(String algorithm) {
        log.debug("Creating strategy for algorithm: {}", algorithm);
        return switch (algorithm.toLowerCase()) {
            case "simple" -> {
                log.debug("Selected Simple strategy");
                yield new LoadingStrategySimple();
            }
            case "maxdense" -> {
                log.debug("Selected MaxDense strategy");
                yield new LoadingStrategyMaxDense();
            }
            default -> {
                log.error("Unknown algorithm: {}", algorithm);
                throw new IllegalArgumentException(
                        "Unknown algorithm: " + algorithm + ". Available: simple, maxdense"
                );
            }
        };
    }

    /**
     * Выводит результат загрузки на экран.
     *
     * @param trucks список машин
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
     * Выводит статистику загрузки.
     *
     * @param trucks список машин
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