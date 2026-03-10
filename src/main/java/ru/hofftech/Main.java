package ru.hofftech;

import ru.hofftech.model.Parcel;
import ru.hofftech.file.ParcelFileReader;
import ru.hofftech.service.TruckLoader;
import ru.hofftech.model.Truck;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\7101595\\Desktop\\java\\test.txt";

        ParcelFileReader reader = new ParcelFileReader();

        try {
            List<Parcel> parcels = reader.readFromFile(filePath);
            System.out.println("\nFile successfully loaded!");
            reader.printParcels(parcels);

            TruckLoader loader = new TruckLoader();

            System.out.println("\n\n=== TESTING SIMPLE ALGORITHM ===");
            List<Truck> simpleTrucks = loader.loadParcels(parcels, false);
            loader.printTrucks(simpleTrucks);

            System.out.println("\n\n=== TESTING OPTIMAL ALGORITHM ===");
            List<Truck> optimalTrucks = loader.loadParcels(parcels, true);
            loader.printTrucks(optimalTrucks);

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.err.println("Check path: " + filePath);
        }
    }
}