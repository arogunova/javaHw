package ru.hofftech.service;

import ru.hofftech.model.Parcel;
import ru.hofftech.model.PlacementResult;
import ru.hofftech.model.Truck;

import java.util.ArrayList;
import java.util.List;

/**
 * Стратегия максимально плотной загрузки.
 * Стараемся использовать минимум машин, максимально заполняя каждую.

 * Алгоритм:
 * 1. Сортируем посылки от больших к маленьким (жадный алгоритм)
 * 2. Для каждой посылки пытаемся найти место в существующих машинах
 * 3. Если не влезает ни в одну - создаем новую машину

 * Плюсы: эффективно использует пространство
 * Минусы: более сложный алгоритм
 */
public class LoadingStrategyMaxDense implements LoadingStrategy {

    @Override
    public List<Truck> load(List<Parcel> parcels) {
        System.out.println("\n--- USING MAX DENSE LOADING STRATEGY ---");
        System.out.println("Trying to pack parcels as tightly as possible");

        // Шаг 1: Сортируем посылки от больших к маленьким
        List<Parcel> sortedParcels = new ArrayList<>(parcels);
        sortedParcels.sort((p1, p2) -> Integer.compare(p2.getArea(), p1.getArea()));

        System.out.println("\nParcels sorted by size (largest first):");
        for (Parcel p : sortedParcels) {
            System.out.println("  " + p);
        }

        // Шаг 2: Создаем список машин (пока пустой)
        List<Truck> trucks = new ArrayList<>();

        // Шаг 3: Проходим по всем посылкам
        for (int i = 0; i < sortedParcels.size(); i++) {
            Parcel parcel = sortedParcels.get(i);
            System.out.println("\nProcessing parcel " + (i + 1) + ": " + parcel);

            boolean placed = false;

            // Шаг 4: Пробуем разместить в существующих машинах
            for (int t = 0; t < trucks.size(); t++) {
                Truck truck = trucks.get(t);
                System.out.println("  Trying existing truck #" + (t + 1));

                PlacementResult result = truck.findPositionSimple(parcel);

                if (result.isFound()) {
                    truck.placePackage(parcel, result);
                    System.out.println("    Placed at (" + result.getX() + "," + result.getY() + ")");
                    placed = true;
                    break;
                } else {
                    System.out.println("    No space in this truck");
                }
            }

            // Шаг 5: Если не разместили - создаем новую машину
            if (!placed) {
                System.out.println("  No space in existing trucks, creating new truck #" + (trucks.size() + 1));

                Truck newTruck = new Truck();
                PlacementResult result = newTruck.findPositionSimple(parcel);

                if (result.isFound()) {
                    newTruck.placePackage(parcel, result);
                    System.out.println("    Placed at (" + result.getX() + "," + result.getY() + ")");
                    trucks.add(newTruck);
                } else {
                    System.err.println("    ERROR: Cannot place parcel even in empty truck!");
                }
            }
        }

        System.out.println("\n--- MAX DENSE STRATEGY FINISHED ---");
        System.out.println("Trucks used: " + trucks.size());

        return trucks;
    }
}