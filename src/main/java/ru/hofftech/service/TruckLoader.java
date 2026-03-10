package ru.hofftech.service;

// Импортируем наши классы из других пакетов
import ru.hofftech.model.Parcel;
import ru.hofftech.model.Truck;
import ru.hofftech.model.PlacementResult;

// Стандартные импорты Java
import java.util.ArrayList;
import java.util.List;

/**
 * Класс TruckLoader (Загрузчик машин) - содержит ЛОГИКУ загрузки посылок в грузовики.
 *
 * Это главный класс, который РЕАЛЬНО РАСКЛАДЫВАЕТ посылки по машинам.
 * Если Parcel и Truck - это просто "чертежи" (описания), то TruckLoader - это
 * "рабочий", который берет эти чертежи и делает дело.
 *
 * Что делает этот класс:
 * 1. Принимает список посылок
 * 2. Раскладывает их по машинам (каждая машина 6x6)
 * 3. Если посылка не влезает в текущую машину - создает новую
 * 4. Поддерживает два алгоритма загрузки (простой и оптимальный)
 */
public class TruckLoader {

    // =============== ОСНОВНОЙ ПУБЛИЧНЫЙ МЕТОД ===============

    /**
     * Главный метод класса - загружает все посылки в машины.
     * Именно этот метод мы будем вызывать из Main.
     *
     * @param parcels список посылок для загрузки
     * @param useOptimization true - использовать оптимальный алгоритм,
     *                        false - использовать простой алгоритм
     * @return список грузовиков с загруженными посылками
     */
    public List<Truck> loadParcels(List<Parcel> parcels, boolean useOptimization) {
        System.out.println("\n=== STARTING TRUCK LOADING ===");
        System.out.println("Total parcels to load: " + parcels.size());
        System.out.println("Algorithm: " + (useOptimization ? "OPTIMAL (плотная упаковка)" : "SIMPLE (одна посылка - одна машина)"));

        // В зависимости от выбранного алгоритма вызываем соответствующий метод
        if (useOptimization) {
            // Оптимальный алгоритм - пытаемся упаковать плотно
            return loadOptimally(parcels);
        } else {
            // Простой алгоритм - каждая посылка в отдельной машине
            return loadSimply(parcels);
        }
    }

    // =============== ПРОСТОЙ АЛГОРИТМ ===============

    /**
     * ПРОСТОЙ АЛГОРИТМ: каждая посылка - в отдельную машину.
     * Это самый примитивный вариант, который легко реализовать.
     *
     * Как работает:
     * 1. Берем первую посылку
     * 2. Создаем для нее новую машину
     * 3. Кладем посылку в эту машину
     * 4. Повторяем для всех посылок
     *
     * Плюсы: очень простой код
     * Минусы: очень неэффективно (много машин)
     *
     * @param parcels список посылок
     * @return список машин (по одной на каждую посылку)
     */
    private List<Truck> loadSimply(List<Parcel> parcels) {
        System.out.println("\n--- USING SIMPLE ALGORITHM ---");
        System.out.println("Each parcel will get its own truck");

        // Создаем список для хранения всех машин
        List<Truck> trucks = new ArrayList<>();

        // Проходим по всем посылкам
        for (int i = 0; i < parcels.size(); i++) {
            Parcel parcel = parcels.get(i);
            System.out.println("\nProcessing parcel " + (i + 1) + ": " + parcel);

            // Шаг 1: Создаем новую машину для каждой посылки
            Truck truck = new Truck();
            System.out.println("  Created new truck #" + (trucks.size() + 1));

            // Шаг 2: Ищем место для посылки в новой машине
            // Используем простой алгоритм поиска (первое свободное место)
            PlacementResult result = truck.findPositionSimple(parcel);

            // Шаг 3: Проверяем, что место найдено
            if (result.isFound()) {
                // Размещаем посылку
                truck.placePackage(parcel, result);
                System.out.println("  Placed parcel at (" + result.getX() + "," + result.getY() + ")");

                // Добавляем машину в список
                trucks.add(truck);
            } else {
                // Это не должно случиться в пустой машине!
                System.err.println("  ERROR: Cannot place parcel even in empty truck!");
            }
        }

        System.out.println("\n--- SIMPLE ALGORITHM FINISHED ---");
        System.out.println("Trucks used: " + trucks.size());

        return trucks;
    }

    // =============== ОПТИМАЛЬНЫЙ АЛГОРИТМ ===============

    /**
     * ОПТИМАЛЬНЫЙ АЛГОРИТМ: пытаемся упаковать посылки как можно плотнее.
     *
     * Как работает:
     * 1. Сортируем посылки от больших к маленьким (жадный алгоритм)
     * 2. Для каждой посылки пытаемся найти место в существующих машинах
     * 3. Если не влезает ни в одну - создаем новую машину
     *
     * Почему сортируем большие сначала?
     * - Большие посылки сложнее разместить, их нужно ставить первыми
     * - Маленькие посылки легко вписать в оставшиеся дырки
     *
     * @param parcels список посылок
     * @return список машин (оптимально загруженных)
     */
    private List<Truck> loadOptimally(List<Parcel> parcels) {
        System.out.println("\n--- USING OPTIMAL ALGORITHM ---");
        System.out.println("Trying to pack parcels efficiently");

        // Шаг 1: СОРТИРУЕМ ПОСЫЛКИ (большие первые)
        // Создаем копию списка, чтобы не изменять оригинал
        List<Parcel> sortedParcels = new ArrayList<>(parcels);

        // Сортируем по убыванию площади
        // Чем больше площадь - тем раньше ставим
        sortedParcels.sort((p1, p2) -> {
            // Сравниваем: p2 с p1 чтобы получить убывание (большие первые)
            return Integer.compare(p2.getArea(), p1.getArea());
        });

        System.out.println("\nParcels sorted by size (largest first):");
        for (Parcel p : sortedParcels) {
            System.out.println("  " + p);
        }

        // Шаг 2: СОЗДАЕМ СПИСОК МАШИН (пока пустой)
        List<Truck> trucks = new ArrayList<>();

        // Шаг 3: ПРОХОДИМ ПО ВСЕМ ПОСЫЛКАМ
        for (int i = 0; i < sortedParcels.size(); i++) {
            Parcel parcel = sortedParcels.get(i);
            System.out.println("\nProcessing parcel " + (i + 1) + ": " + parcel);

            boolean placed = false; // флаг: удалось ли разместить посылку

            // Шаг 4: ПРОБУЕМ РАЗМЕСТИТЬ В СУЩЕСТВУЮЩИХ МАШИНАХ
            for (int t = 0; t < trucks.size(); t++) {
                Truck truck = trucks.get(t);
                System.out.println("  Trying existing truck #" + (t + 1));

                // Ищем место в этой машине
                PlacementResult result = truck.findPositionSimple(parcel);

                if (result.isFound()) {
                    // Нашли место - размещаем
                    truck.placePackage(parcel, result);
                    System.out.println("    Placed at (" + result.getX() + "," + result.getY() + ")");
                    placed = true;
                    break; // выходим из цикла по машинам
                } else {
                    System.out.println("    No space in this truck");
                }
            }

            // Шаг 5: ЕСЛИ НЕ РАЗМЕСТИЛИ НИ В ОДНОЙ МАШИНЕ - СОЗДАЕМ НОВУЮ
            if (!placed) {
                System.out.println("  No space in existing trucks, creating new truck #" + (trucks.size() + 1));

                // Создаем новую машину
                Truck newTruck = new Truck();

                // Ищем место в новой машине
                PlacementResult result = newTruck.findPositionSimple(parcel);

                if (result.isFound()) {
                    // Размещаем посылку
                    newTruck.placePackage(parcel, result);
                    System.out.println("    Placed at (" + result.getX() + "," + result.getY() + ")");

                    // Добавляем новую машину в список
                    trucks.add(newTruck);
                } else {
                    // Этого не должно случиться в пустой машине!
                    System.err.println("    ERROR: Cannot place parcel even in empty truck!");
                }
            }
        }

        System.out.println("\n--- OPTIMAL ALGORITHM FINISHED ---");
        System.out.println("Trucks used: " + trucks.size());

        return trucks;
    }

    // =============== МЕТОДЫ ДЛЯ ВЫВОДА РЕЗУЛЬТАТОВ ===============

    /**
     * Печатает все загруженные машины в красивом формате.
     *
     * @param trucks список машин для печати
     */
    public void printTrucks(List<Truck> trucks) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("LOADING RESULT");
        System.out.println("=".repeat(50));

        // Проверяем, есть ли машины
        if (trucks == null || trucks.isEmpty()) {
            System.out.println("No trucks used (no parcels?)");
            return;
        }

        // Выводим каждую машину
        System.out.println("Total trucks used: " + trucks.size());

        for (int i = 0; i < trucks.size(); i++) {
            System.out.println("\nTRUCK #" + (i + 1));
            System.out.println(trucks.get(i));
        }

        // Считаем и выводим статистику
        printStatistics(trucks);
    }

    /**
     * Считает статистику загрузки (сколько машин, посылок, заполненность).
     *
     * @param trucks список машин
     */
    private void printStatistics(List<Truck> trucks) {
        System.out.println("\n" + "-".repeat(30));
        System.out.println("STATISTICS");
        System.out.println("-".repeat(30));

        int totalTrucks = trucks.size();
        int totalParcels = 0;
        int totalCells = totalTrucks * Truck.SIZE * Truck.SIZE;

        // Считаем общее количество посылок
        for (Truck truck : trucks) {
            totalParcels += truck.getPackagesCount();
        }

        System.out.println("Total trucks: " + totalTrucks);
        System.out.println("Total parcels: " + totalParcels);
        System.out.println("Total capacity (cells): " + totalCells);

        // Здесь можно добавить подсчет занятых клеток
        // Но для этого нужен метод в Truck, который считает занятые клетки
        System.out.println("(Cell usage statistics will be added later)");
    }
}