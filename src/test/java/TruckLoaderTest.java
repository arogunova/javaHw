import org.junit.jupiter.api.*;
import ru.hofftech.model.Parcel;
import ru.hofftech.model.Truck;
import ru.hofftech.model.PlacementResult;
import ru.hofftech.service.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class TruckLoaderTest {

    private TruckLoader loader;
    private List<Parcel> testParcels;

    @BeforeEach
    void setUp() {
        loader = new TruckLoader();
        testParcels = createTestParcels();
    }

    @AfterEach
    void tearDown() {
        loader = null;
        testParcels = null;
    }

    // =============== ТЕСТЫ ДЛЯ SIMPLE СТРАТЕГИИ ===============

    @Test
    @DisplayName("Simple стратегия: одна посылка - одна машина")
    void testSimpleStrategy() {
        // Создаем стратегию напрямую
        LoadingStrategy strategy = new LoadingStrategySimple();

        // Загружаем
        List<Truck> trucks = strategy.load(testParcels);

        // Проверяем
        assertThat(trucks)
                .as("Simple стратегия должна создать по машине на каждую посылку")
                .hasSize(3);

        for (int i = 0; i < trucks.size(); i++) {
            assertThat(trucks.get(i).getPackagesCount())
                    .as("Машина %d должна содержать 1 посылку", i + 1)
                    .isEqualTo(1);
        }
    }

    // =============== ТЕСТЫ ДЛЯ MAX DENSE СТРАТЕГИИ ===============

    @Test
    @DisplayName("MaxDense стратегия: три посылки 3x3 должны поместиться в одну машину")
    void testMaxDenseStrategy() {
        // Создаем стратегию напрямую
        LoadingStrategy strategy = new LoadingStrategyMaxDense();

        // Загружаем
        List<Truck> trucks = strategy.load(testParcels);

        // Проверяем - должно быть 1 машина с 3 посылками
        assertThat(trucks)
                .as("MaxDense стратегия должна упаковать 3 посылки в 1 машину")
                .hasSize(1);

        assertThat(trucks.getFirst().getPackagesCount())
                .as("В машине должно быть 3 посылки")
                .isEqualTo(3);
    }

    // =============== ТЕСТЫ ЧЕРЕЗ TRUCKLOADER ===============

    @Test
    @DisplayName("TruckLoader с simple алгоритмом")
    void testTruckLoaderWithSimple() {
        List<Truck> trucks = loader.loadParcels(testParcels, "simple");

        assertThat(trucks).hasSize(3);
        assertThat(trucks.getFirst().getPackagesCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("TruckLoader с maxdense алгоритмом")
    void testTruckLoaderWithMaxDense() {
        List<Truck> trucks = loader.loadParcels(testParcels, "maxdense");

        assertThat(trucks).hasSize(1);
        assertThat(trucks.getFirst().getPackagesCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("TruckLoader с неизвестным алгоритмом должен кидать исключение")
    void testTruckLoaderWithUnknownAlgorithm() {
        assertThatThrownBy(() -> {
            loader.loadParcels(testParcels, "alien");
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown algorithm");
    }

    // =============== ТЕСТЫ ДЛЯ ПРАВИЛА ОПОРЫ ===============

    @Test
    @DisplayName("Проверка правила опоры (>50% основания)")
    void testSupportRule() {
        // Создаем основание 2x2
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

        // Должно быть можно поставить прямо над основанием (2 клетки опоры)
        boolean canPlaceGood = truck.canPlace(longParcel, 0, 3);
        assertThat(canPlaceGood)
                .as("Посылка с опорой на 2 клетки из 2 должна ставиться")
                .isTrue();

        // Должно быть нельзя поставить со сдвигом (1 клетка опоры)
        boolean canPlaceBad = truck.canPlace(longParcel, 1, 3);
        assertThat(canPlaceBad)
                .as("Посылка с опорой на 1 клетку из 2 НЕ должна ставиться")
                .isFalse();
    }

    // =============== ТЕСТЫ ДЛЯ ГРАНИЧНЫХ СЛУЧАЕВ ===============

    @Test
    @DisplayName("Пустой список посылок")
    void testEmptyList() {
        LoadingStrategy strategy = new LoadingStrategySimple();
        List<Truck> trucks = strategy.load(new ArrayList<>());

        assertThat(trucks)
                .as("Пустой список должен вернуть пустой список машин")
                .isEmpty();
    }

    @Test
    @DisplayName("Посылка точно по размеру кузова (6x6)")
    void testExactFit() {
        // Создаем посылку 6x6
        List<String> fullShape = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            fullShape.add("111111");
        }
        Parcel fullParcel = new Parcel(fullShape, '1');

        List<Parcel> oneParcel = new ArrayList<>();
        oneParcel.add(fullParcel);

        // Пробуем обе стратегии
        LoadingStrategy simple = new LoadingStrategySimple();
        LoadingStrategy dense = new LoadingStrategyMaxDense();

        List<Truck> simpleTrucks = simple.load(oneParcel);
        List<Truck> denseTrucks = dense.load(oneParcel);

        // Обе должны создать одну машину
        assertThat(simpleTrucks).hasSize(1);
        assertThat(denseTrucks).hasSize(1);

        assertThat(simpleTrucks.getFirst().getPackagesCount()).isEqualTo(1);
        assertThat(denseTrucks.getFirst().getPackagesCount()).isEqualTo(1);
    }

    // =============== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ===============

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