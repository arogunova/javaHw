package ru.hofftech.json;

import ru.hofftech.model.Parcel;
import ru.hofftech.model.Truck;

import java.util.ArrayList;
import java.util.List;

/**
 * Конвертер между нашими моделями (Truck, Parcel) и JSON-моделями.
 */
public class JsonConverter {

    /**
     * Преобразует список машин в JSON-модель для сохранения.
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
     * Преобразует одну машину в TruckJson.
     */
    private static TruckJson truckToJson(Truck truck) {
        List<ParcelJson> parcelJsons = new ArrayList<>();

        // Получаем информацию о посылках из Truck
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
     * Преобразует посылку в ParcelJson.
     */
    private static ParcelJson parcelToJson(Parcel parcel, int x, int y) {
        return new ParcelJson(parcel.getShape(), x, y);
    }

    /**
     * Преобразует JSON-модель обратно в список машин.
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
     * Преобразует TruckJson обратно в Truck.
     */
    private static Truck truckFromJson(TruckJson truckJson) {
        Truck truck = new Truck();

        for (ParcelJson parcelJson : truckJson.getParcels()) {
            // Находим первый непробельный символ в форме
            char symbol = findSymbol(parcelJson.getShape());

            // Создаем посылку с правильным символом
            Parcel parcel = new Parcel(parcelJson.getShape(), symbol);

            // Получаем координаты
            int x = parcelJson.getX();
            int y = parcelJson.getY();

            // Размещаем посылку в машине
            truck.placePackageAt(parcel, x, y);
        }

        return truck;
    }

    private static char findSymbol(List<String> shape) {
        for (String line : shape) {
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (!Character.isWhitespace(c)) {
                    return c;  // нашли символ!
                }
            }
        }
        throw new IllegalArgumentException("No visible symbol in shape");
    }
}