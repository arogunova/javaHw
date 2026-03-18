package ru.hofftech.json;

import ru.hofftech.model.Parcel;
import ru.hofftech.model.Truck;

import java.util.ArrayList;
import java.util.List;

public class JsonConverter {

    public static LoadingResultJson toJson(List<Truck> trucks) {
        List<TruckJson> truckJsons = new ArrayList<>();

        for (Truck truck : trucks) {
            TruckJson truckJson = truckToJson(truck);
            truckJsons.add(truckJson);
        }

        return new LoadingResultJson(truckJsons);
    }

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

    private static ParcelJson parcelToJson(Parcel parcel, int x, int y) {
        return new ParcelJson(parcel.getShape(), x, y);
    }

    public static List<Truck> fromJson(LoadingResultJson result) {
        List<Truck> trucks = new ArrayList<>();

        for (TruckJson truckJson : result.getTrucks()) {
            Truck truck = truckFromJson(truckJson);
            trucks.add(truck);
        }

        return trucks;
    }

    private static Truck truckFromJson(TruckJson truckJson) {
        Truck truck = new Truck();

        for (ParcelJson parcelJson : truckJson.getParcels()) {
            char symbol = findSymbol(parcelJson.getShape());

            Parcel parcel = new Parcel(parcelJson.getShape(), symbol);

            int x = parcelJson.getX();
            int y = parcelJson.getY();

            truck.placePackageAt(parcel, x, y);
        }

        return truck;
    }

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