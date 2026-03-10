import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.hofftech.model.Parcel;
import ru.hofftech.model.Truck;
import ru.hofftech.model.PlacementResult;
import ru.hofftech.service.TruckLoader;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты для TruckLoader с использованием JUnit 5 и AssertJ.

 * Проверяем:
 * 1. Простой алгоритм (одна посылка - одна машина)
 * 2. Оптимальный алгоритм с разными комбинациями посылок
 * 3. Правило опоры (>50% основания)
 * 4. Граничные случаи
 */
@TestMethodOrder(OrderAnnotation.class)
public class TruckLoaderTest {

    private TruckLoader loader;
    private List<Parcel> testParcels;

    @BeforeEach
    void setUp() {
        loader = new TruckLoader();
        testParcels = createTestParcels();
        System.out.println("\n🔧 Setup completed");
    }

    @AfterEach
    void tearDown() {
        loader = null;
        testParcels = null;
        System.out.println("🧹 Cleanup completed");
    }

    // =============== ТЕСТ 1: ПРОСТОЙ АЛГОРИТМ ===============

    @Test
    @Order(1)
    @DisplayName("Простой алгоритм: одна посылка - одна машина")
    void testSimpleAlgorithm() {
        System.out.println("\n📋 ТЕСТ: Простой алгоритм");

        // When - загружаем простым алгоритмом
        List<Truck> trucks = loader.loadParcels(testParcels, false);

        // Then - проверяем результаты
        assertThat(trucks)
                .as("Простой алгоритм должен создать по машине на каждую посылку")
                .isNotNull()
                .hasSize(3);

        // Проверяем, что в каждой машине по одной посылке
        for (int i = 0; i < trucks.size(); i++) {
            assertThat(trucks.get(i).getPackagesCount())
                    .as("Машина %d должна содержать 1 посылку", i + 1)
                    .isEqualTo(1);
        }

        System.out.println("✅ Тест пройден: создано " + trucks.size() + " машин");
    }

    // =============== ТЕСТ 2: ДВЕ ПОСЫЛКИ 3x3 ===============

    @Test
    @Order(2)
    @DisplayName("Оптимальный алгоритм: две посылки 3x3 должны поместиться в ОДНУ машину")
    void testTwoLargeParcelsFitInOneTruck() {
        System.out.println("\n📋 ТЕСТ: Две посылки 3x3 должны поместиться в одну машину");

        // Given - создаем две посылки 3x3
        List<Parcel> parcels = new ArrayList<>();

        // Посылка '9' 3x3
        List<String> shape1 = new ArrayList<>();
        shape1.add("999");
        shape1.add("999");
        shape1.add("999");
        parcels.add(new Parcel(shape1, '9'));

        // Посылка '8' 3x3
        List<String> shape2 = new ArrayList<>();
        shape2.add("888");
        shape2.add("888");
        shape2.add("888");
        parcels.add(new Parcel(shape2, '8'));

        // When - загружаем оптимальным алгоритмом
        List<Truck> trucks = loader.loadParcels(parcels, true);

        // Then - должна быть 1 машина с 2 посылками
        assertThat(trucks)
                .as("Две посылки 3x3 должны влезть в одну машину 6x6")
                .hasSize(1);

        assertThat(trucks.getFirst().getPackagesCount())
                .as("В машине должно быть 2 посылки")
                .isEqualTo(2);

        // Выводим результат для наглядности
        System.out.println("\nРезультат загрузки:");
        System.out.println(trucks.getFirst());

        System.out.println("✅ Тест пройден: обе посылки поместились в одну машину");
    }

    // =============== ТЕСТ 3: ТРИ ПОСЫЛКИ 3x3 ===============

    @Test
    @Order(3)
    @DisplayName("Оптимальный алгоритм: три посылки 3x3 должны поместиться в ОДНУ машину")
    void testThreeLargeParcelsFitInOneTruck() {
        System.out.println("\n📋 ТЕСТ: Три посылки 3x3 должны поместиться в одну машину");

        // Given - создаем три посылки 3x3
        List<Parcel> parcels = new ArrayList<>();
        char[] symbols = {'9', '8', '7'};

        for (char symbol : symbols) {
            List<String> shape = new ArrayList<>();
            shape.add("" + symbol + symbol + symbol);
            shape.add("" + symbol + symbol + symbol);
            shape.add("" + symbol + symbol + symbol);
            parcels.add(new Parcel(shape, symbol));
        }

        // When - загружаем оптимальным алгоритмом
        List<Truck> trucks = loader.loadParcels(parcels, true);

        // Then - должна быть 1 машина
        assertThat(trucks)
                .as("Три посылки 3x3 должны поместиться в одну машину (27 из 36 клеток)")
                .hasSize(1);

        // Проверяем общее количество посылок
        int totalPackages = 0;
        for (Truck truck : trucks) {
            totalPackages += truck.getPackagesCount();
        }

        assertThat(totalPackages)
                .as("Всего должно быть 3 посылки")
                .isEqualTo(3);

        // Выводим результат
        System.out.println("\nРезультат загрузки (1 машина с 3 посылками):");
        System.out.println(trucks.getFirst());

        System.out.println("✅ Тест пройден: все три посылки поместились в одну машину");
    }

    // =============== ТЕСТ 4: ЧЕТЫРЕ ПОСЫЛКИ 3x3 ===============

    @Test
    @Order(4)
    @DisplayName("Оптимальный алгоритм: четыре посылки 3x3 должны поместиться в ОДНУ машину")
    void testFourLargeParcelsFitInOneTruck() {
        System.out.println("\n📋 ТЕСТ: Четыре посылки 3x3 должны поместиться в одну машину");

        // Given - создаем четыре посылки 3x3
        List<Parcel> parcels = new ArrayList<>();
        char[] symbols = {'9', '8', '7', '6'};

        for (char symbol : symbols) {
            List<String> shape = new ArrayList<>();
            shape.add("" + symbol + symbol + symbol);
            shape.add("" + symbol + symbol + symbol);
            shape.add("" + symbol + symbol + symbol);
            parcels.add(new Parcel(shape, symbol));
        }

        // When - загружаем оптимальным алгоритмом
        List<Truck> trucks = loader.loadParcels(parcels, true);

        // Then - должна быть 1 машина (36 клеток - ровно размер машины)
        assertThat(trucks)
                .as("Четыре посылки 3x3 занимают ровно 36 клеток - одна машина")
                .hasSize(1);

        assertThat(trucks.getFirst().getPackagesCount())
                .as("В машине должно быть 4 посылки")
                .isEqualTo(4);

        System.out.println("\nРезультат загрузки (идеальное заполнение):");
        System.out.println(trucks.getFirst());

        System.out.println("✅ Тест пройден: 4 посылки в одной машине");
    }

    // =============== ТЕСТ 5: ПРАВИЛО ОПОРЫ ===============

    @Test
    @Order(5)
    @DisplayName("Проверка правила опоры (>50% основания)")
    void testSupportRule() {
        System.out.println("\n📋 ТЕСТ: Проверка правила опоры");

        // Given - создаем основание 2x2
        List<String> baseShape = new ArrayList<>();
        baseShape.add("XX");
        baseShape.add("XX");
        Parcel base = new Parcel(baseShape, 'X');

        // Создаем длинную посылку 2x1
        List<String> longShape = new ArrayList<>();
        longShape.add("YY");
        Parcel longParcel = new Parcel(longShape, 'Y');

        // Создаем машину и ставим основание
        Truck truck = new Truck();
        PlacementResult baseResult = truck.findPositionSimple(base);
        truck.placePackage(base, baseResult);

        System.out.println("Основание X (2x2) поставлено в позиции (0,4):");
        truck.printDebug();

        // Test 1: Ставим длинную посылку прямо над основанием (полная опора)
        System.out.println("\nПроверка 1: Посылка прямо над основанием (2 клетки опоры)");
        boolean canPlaceGood = truck.canPlace(longParcel, 0, 3);

        assertThat(canPlaceGood)
                .as("Посылка с опорой на 2 клетки из 2 (100%) должна ставиться")
                .isTrue();

        System.out.println("  ✅ Можно поставить (опора 2/2 = 100% > 50%)");

        // Test 2: Ставим со сдвигом (только 1 клетка опоры)
        System.out.println("\nПроверка 2: Посылка со сдвигом (1 клетка опоры)");
        boolean canPlaceBad = truck.canPlace(longParcel, 1, 3);

        assertThat(canPlaceBad)
                .as("Посылка с опорой на 1 клетку из 2 (50%) НЕ должна ставиться")
                .isFalse();

        System.out.println("  ✅ Нельзя поставить (опора 1/2 = 50%, а нужно >50%)");

        System.out.println("✅ Тест пройден: правило опоры работает правильно");
    }

    // =============== ТЕСТ 6: СМЕШАННЫЕ ПОСЫЛКИ ===============

    @Test
    @Order(6)
    @DisplayName("Оптимальный алгоритм: смешанные посылки разных размеров")
    void testMixedParcels() {
        System.out.println("\n📋 ТЕСТ: Смешанные посылки разных размеров");

        // Given - создаем посылки разных размеров
        List<Parcel> parcels = new ArrayList<>();

        // Большая 3x3
        List<String> shape1 = new ArrayList<>();
        shape1.add("999");
        shape1.add("999");
        shape1.add("999");
        parcels.add(new Parcel(shape1, '9'));

        // Средняя 2x2
        List<String> shape2 = new ArrayList<>();
        shape2.add("66");
        shape2.add("66");
        parcels.add(new Parcel(shape2, '6'));

        // Две маленькие 1x1
        List<String> shape3 = new ArrayList<>();
        shape3.add("1");
        parcels.add(new Parcel(shape3, '1'));

        List<String> shape4 = new ArrayList<>();
        shape4.add("2");
        parcels.add(new Parcel(shape4, '2'));

        // When - загружаем
        List<Truck> trucks = loader.loadParcels(parcels, true);

        // Then - проверяем, что все загрузилось
        assertThat(trucks)
                .as("Должна быть минимум 1 машина")
                .isNotEmpty();

        int totalPackages = 0;
        for (Truck truck : trucks) {
            totalPackages += truck.getPackagesCount();
        }

        assertThat(totalPackages)
                .as("Всего должно быть загружено 4 посылки")
                .isEqualTo(4);

        // Выводим результат
        System.out.println("\nРезультат загрузки (" + trucks.size() + " машин):");
        for (int i = 0; i < trucks.size(); i++) {
            System.out.println("\nМашина " + (i + 1) + ":");
            System.out.println(trucks.get(i));
        }

        System.out.println("✅ Тест пройден: все посылки загружены");
    }

    // =============== ТЕСТ 7: ПУСТОЙ СПИСОК ===============

    @Test
    @Order(7)
    @DisplayName("Пустой список посылок")
    void testEmptyList() {
        System.out.println("\n📋 ТЕСТ: Пустой список посылок");

        List<Parcel> emptyList = new ArrayList<>();

        List<Truck> trucks = loader.loadParcels(emptyList, true);

        assertThat(trucks)
                .as("Пустой список посылок должен вернуть пустой список машин")
                .isEmpty();

        System.out.println("✅ Тест пройден: машин не создано");
    }

    // =============== ТЕСТ 8: ТОЧНОЕ ЗАПОЛНЕНИЕ ===============

    @Test
    @Order(8)
    @DisplayName("Посылка точно по размеру кузова (6x6)")
    void testExactFit() {
        System.out.println("\n📋 ТЕСТ: Посылка 6x6 точно по размеру кузова");

        // Создаем посылку 6x6
        List<String> fullShape = new ArrayList<>();
        fullShape.add("111111");
        fullShape.add("111111");
        fullShape.add("111111");
        fullShape.add("111111");
        fullShape.add("111111");
        fullShape.add("111111");
        Parcel fullParcel = new Parcel(fullShape, '1');

        List<Parcel> oneParcel = new ArrayList<>();
        oneParcel.add(fullParcel);

        // When
        List<Truck> trucks = loader.loadParcels(oneParcel, true);

        // Then
        assertThat(trucks)
                .as("Посылка 6x6 должна занять ровно одну машину")
                .hasSize(1);

        assertThat(trucks.getFirst().getPackagesCount())
                .as("В машине должна быть 1 посылка")
                .isEqualTo(1);

        System.out.println("Результат:");
        System.out.println(trucks.getFirst());

        System.out.println("✅ Тест пройден: посылка точно вписалась в кузов");
    }

    // =============== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ===============

    /**
     * Создает стандартный набор тестовых посылок:
     * - 9 (3x3)
     * - 6 (2x2)
     * - 1 (1x1)
     */
    private List<Parcel> createTestParcels() {
        List<Parcel> parcels = new ArrayList<>();

        // Посылка 9 (3x3)
        List<String> shape1 = new ArrayList<>();
        shape1.add("999");
        shape1.add("999");
        shape1.add("999");
        parcels.add(new Parcel(shape1, '9'));

        // Посылка 6 (2x2)
        List<String> shape2 = new ArrayList<>();
        shape2.add("66");
        shape2.add("66");
        parcels.add(new Parcel(shape2, '6'));

        // Посылка 1 (1x1)
        List<String> shape3 = new ArrayList<>();
        shape3.add("1");
        parcels.add(new Parcel(shape3, '1'));

        return parcels;
    }
}