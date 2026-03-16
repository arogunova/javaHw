package ru.hofftech.service;

import ru.hofftech.model.Parcel;
import ru.hofftech.model.PlacementResult;
import ru.hofftech.model.Truck;

import java.util.ArrayList;
import java.util.List;

/**
 * Простая стратегия загрузки: каждая посылка в отдельной машине.

 * Алгоритм:
 * 1. Для каждой посылки создаем новую машину
 * 2. Ищем место (первое свободное)
 * 3. Размещаем посылку

 * Плюсы: очень простой, гарантированно работает
 * Минусы: неэффективно использует пространство
 */
public class LoadingStrategySimple implements LoadingStrategy {

    @Override
    public List<Truck> load(List<Parcel> parcels) {
        System.out.println("\n--- USING SIMPLE LOADING STRATEGY ---");
        System.out.println("Each parcel will get its own truck");

        // Список машин, которые будем возвращать
        List<Truck> trucks = new ArrayList<>();

        // Проходим по всем посылкам
        for (int i = 0; i < parcels.size(); i++) {
            Parcel parcel = parcels.get(i);
            System.out.println("\nProcessing parcel " + (i + 1) + ": " + parcel);

            // Шаг 1: Создаем новую машину для каждой посылки
            Truck truck = new Truck();
            System.out.println("  Created new truck #" + (trucks.size() + 1));

            // Шаг 2: Ищем место для посылки в новой машине
            PlacementResult result = truck.findPositionSimple(parcel);

            // Шаг 3: Проверяем, что место найдено
            if (result.isFound()) {
                // Размещаем посылку
                truck.placePackage(parcel, result);
                System.out.println("  Placed parcel at (" + result.getX() + "," + result.getY() + ")");

                // Добавляем машину в список
                trucks.add(truck);
            } else {
                // Этого не должно случиться в пустой машине!
                System.err.println("  ERROR: Cannot place parcel even in empty truck!");
            }
        }

        System.out.println("\n--- SIMPLE STRATEGY FINISHED ---");
        System.out.println("Trucks used: " + trucks.size());

        return trucks;
    }
}