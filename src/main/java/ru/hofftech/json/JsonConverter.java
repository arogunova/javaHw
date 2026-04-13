package ru.hofftech.json;

import ru.hofftech.model.Parcel;
import ru.hofftech.model.Truck;

import java.util.ArrayList;
import java.util.List;

/**
 * Конвертер между моделями Truck/Parcel и JSON-моделями TruckJson/ParcelJson.
 * Обеспечивает сериализацию и десериализацию в/из JSON.
 */
public class JsonConverter {

    /**
     * Преобразует список машин в корневую JSON-модель для сохранения.
     *
     * @param trucks список машин
     * @return корневая JSON-модель
     */
    public static LoadingResultJson toJson(List<Truck> trucks) {
        List<TruckJson> truckJsons = new ArrayList<>();

        for (Truck truck : trucks) {
            TruckJson truckJson = truckToJson(truck);
            truckJsons.add(truckJson);
        }

        return new LoadingResultJson(truckJsons);
    }

    /**
     * Преобразует одну машину в JSON-модель.
     *
     * @param truck машина
     * @return JSON-модель машины
     */
    private static TruckJson truckToJson(Truck truck) {
        List<ParcelJson> parcelJsons = new ArrayList<>();

        List<Object[]> packagesInfo = truck.getPackagesInfo();

        for (Object[] info : packagesInfo) {
            Parcel parcel = (Parcel) info[0];
            int x = (int) info[1];
            int y = (int) info[2];

            ParcelJson parcelJson = parcelToJson(parcel, x, y);
            parcelJsons.add(parcelJson);
        }

        return new TruckJson(parcelJsons);
    }

    /**
     * Преобразует одну посылку в JSON-модель.
     *
     * @param parcel посылка
     * @param x координата X
     * @param y координата Y
     * @return JSON-модель посылки
     */
    private static ParcelJson parcelToJson(Parcel parcel, int x, int y) {
        return new ParcelJson(parcel.getName(), parcel.getShape(), x, y);
    }

    /**
     * Восстанавливает список машин из JSON-модели.
     *
     * @param result корневая JSON-модель
     * @return список машин
     */
    public static List<Truck> fromJson(LoadingResultJson result) {
        List<Truck> trucks = new ArrayList<>();

        for (TruckJson truckJson : result.getTrucks()) {
            Truck truck = truckFromJson(truckJson);
            trucks.add(truck);
        }

        return trucks;
    }

    /**
     * Восстанавливает одну машину из JSON-модели.
     *
     * @param truckJson JSON-модель машины
     * @return машина с размещёнными посылками
     */
    private static Truck truckFromJson(TruckJson truckJson) {
        Truck truck = new Truck();

        for (ParcelJson parcelJson : truckJson.getParcels()) {
            char symbol = findSymbol(parcelJson.getShape());
            String name = parcelJson.getName();
            Parcel parcel = new Parcel(name, parcelJson.getShape(), symbol);
            int x = parcelJson.getX();
            int y = parcelJson.getY();

            truck.placePackageAt(parcel, x, y);
        }

        return truck;
    }

    /**
     * Находит первый непробельный символ в форме посылки.
     *
     * @param shape форма посылки (список строк)
     * @return первый видимый символ
     * @throws IllegalArgumentException если в форме нет видимых символов
     */
    private static char findSymbol(List<String> shape) {
        for (String line : shape) {
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (!Character.isWhitespace(c)) {
                    return c;
                }
            }
        }
        throw new IllegalArgumentException("No visible symbol in shape");
    }
}