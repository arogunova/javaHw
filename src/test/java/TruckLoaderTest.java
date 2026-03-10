import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import ru.hofftech.model.Parcel;
import ru.hofftech.model.Truck;
import ru.hofftech.model.PlacementResult;
import ru.hofftech.service.TruckLoader;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(OrderAnnotation.class)
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

    @Test
    @Order(1)
    @DisplayName("Простой алгоритм: одна посылка - одна машина")
    void testSimpleAlgorithm() {
        List<Truck> trucks = loader.loadParcels(testParcels, false);

        assertThat(trucks)
                .as("Простой алгоритм должен создать по машине на каждую посылку")
                .isNotNull()
                .hasSize(3);

        for (int i = 0; i < trucks.size(); i++) {
            assertThat(trucks.get(i).getPackagesCount())
                    .as("Машина %d должна содержать 1 посылку", i + 1)
                    .isEqualTo(1);
        }
    }

    @Test
    @Order(2)
    @DisplayName("Оптимальный алгоритм: две посылки 3x3 должны поместиться в ОДНУ машину")
    void testTwoLargeParcelsFitInOneTruck() {
        List<Parcel> parcels = new ArrayList<>();

        List<String> shape1 = new ArrayList<>();
        shape1.add("999");
        shape1.add("999");
        shape1.add("999");
        parcels.add(new Parcel(shape1, '9'));

        List<String> shape2 = new ArrayList<>();
        shape2.add("888");
        shape2.add("888");
        shape2.add("888");
        parcels.add(new Parcel(shape2, '8'));

        List<Truck> trucks = loader.loadParcels(parcels, true);

        assertThat(trucks)
                .as("Две посылки 3x3 должны влезть в одну машину 6x6")
                .hasSize(1);

        assertThat(trucks.getFirst().getPackagesCount())
                .as("В машине должно быть 2 посылки")
                .isEqualTo(2);
    }

    @Test
    @Order(3)
    @DisplayName("Оптимальный алгоритм: три посылки 3x3 должны поместиться в ОДНУ машину")
    void testThreeLargeParcelsFitInOneTruck() {
        List<Parcel> parcels = new ArrayList<>();
        char[] symbols = {'9', '8', '7'};

        for (char symbol : symbols) {
            List<String> shape = new ArrayList<>();
            shape.add("" + symbol + symbol + symbol);
            shape.add("" + symbol + symbol + symbol);
            shape.add("" + symbol + symbol + symbol);
            parcels.add(new Parcel(shape, symbol));
        }

        List<Truck> trucks = loader.loadParcels(parcels, true);

        assertThat(trucks)
                .as("Три посылки 3x3 должны поместиться в одну машину")
                .hasSize(1);

        int totalPackages = 0;
        for (Truck truck : trucks) {
            totalPackages += truck.getPackagesCount();
        }

        assertThat(totalPackages)
                .as("Всего должно быть 3 посылки")
                .isEqualTo(3);
    }

    @Test
    @Order(4)
    @DisplayName("Оптимальный алгоритм: четыре посылки 3x3 должны поместиться в ОДНУ машину")
    void testFourLargeParcelsFitInOneTruck() {
        List<Parcel> parcels = new ArrayList<>();
        char[] symbols = {'9', '8', '7', '6'};

        for (char symbol : symbols) {
            List<String> shape = new ArrayList<>();
            shape.add("" + symbol + symbol + symbol);
            shape.add("" + symbol + symbol + symbol);
            shape.add("" + symbol + symbol + symbol);
            parcels.add(new Parcel(shape, symbol));
        }

        List<Truck> trucks = loader.loadParcels(parcels, true);

        assertThat(trucks)
                .as("Четыре посылки 3x3 должны поместиться в одну машину")
                .hasSize(1);

        assertThat(trucks.getFirst().getPackagesCount())
                .as("В машине должно быть 4 посылки")
                .isEqualTo(4);
    }

    @Test
    @Order(5)
    @DisplayName("Проверка правила опоры (>50% основания)")
    void testSupportRule() {
        List<String> baseShape = new ArrayList<>();
        baseShape.add("XX");
        baseShape.add("XX");
        Parcel base = new Parcel(baseShape, 'X');

        List<String> longShape = new ArrayList<>();
        longShape.add("YY");
        Parcel longParcel = new Parcel(longShape, 'Y');

        Truck truck = new Truck();
        PlacementResult baseResult = truck.findPositionSimple(base);
        truck.placePackage(base, baseResult);

        boolean canPlaceGood = truck.canPlace(longParcel, 0, 3);
        assertThat(canPlaceGood)
                .as("Посылка с опорой на 2 клетки из 2 (100%) должна ставиться")
                .isTrue();

        boolean canPlaceBad = truck.canPlace(longParcel, 1, 3);
        assertThat(canPlaceBad)
                .as("Посылка с опорой на 1 клетку из 2 (50%) НЕ должна ставиться")
                .isFalse();
    }

    @Test
    @Order(6)
    @DisplayName("Оптимальный алгоритм: смешанные посылки разных размеров")
    void testMixedParcels() {
        List<Parcel> parcels = new ArrayList<>();

        List<String> shape1 = new ArrayList<>();
        shape1.add("999");
        shape1.add("999");
        shape1.add("999");
        parcels.add(new Parcel(shape1, '9'));

        List<String> shape2 = new ArrayList<>();
        shape2.add("66");
        shape2.add("66");
        parcels.add(new Parcel(shape2, '6'));

        List<String> shape3 = new ArrayList<>();
        shape3.add("1");
        parcels.add(new Parcel(shape3, '1'));

        List<String> shape4 = new ArrayList<>();
        shape4.add("2");
        parcels.add(new Parcel(shape4, '2'));

        List<Truck> trucks = loader.loadParcels(parcels, true);

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
    }

    @Test
    @Order(7)
    @DisplayName("Пустой список посылок")
    void testEmptyList() {
        List<Parcel> emptyList = new ArrayList<>();
        List<Truck> trucks = loader.loadParcels(emptyList, true);

        assertThat(trucks)
                .as("Пустой список посылок должен вернуть пустой список машин")
                .isEmpty();
    }

    @Test
    @Order(8)
    @DisplayName("Посылка точно по размеру кузова (6x6)")
    void testExactFit() {
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

        List<Truck> trucks = loader.loadParcels(oneParcel, true);

        assertThat(trucks)
                .as("Посылка 6x6 должна занять ровно одну машину")
                .hasSize(1);

        assertThat(trucks.getFirst().getPackagesCount())
                .as("В машине должна быть 1 посылка")
                .isEqualTo(1);
    }

    private List<Parcel> createTestParcels() {
        List<Parcel> parcels = new ArrayList<>();

        List<String> shape1 = new ArrayList<>();
        shape1.add("999");
        shape1.add("999");
        shape1.add("999");
        parcels.add(new Parcel(shape1, '9'));

        List<String> shape2 = new ArrayList<>();
        shape2.add("66");
        shape2.add("66");
        parcels.add(new Parcel(shape2, '6'));

        List<String> shape3 = new ArrayList<>();
        shape3.add("1");
        parcels.add(new Parcel(shape3, '1'));

        return parcels;
    }
}