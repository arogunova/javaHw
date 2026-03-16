package ru.hofftech;

import ru.hofftech.model.Parcel;
import ru.hofftech.file.ParcelFileReader;
import ru.hofftech.service.TruckLoader;
import ru.hofftech.model.Truck;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            printUsage();
            return;
        }

        String filePath = args[0];
        String algorithm = args[1];

        if (!isValidAlgorithm(algorithm)) {
            System.err.println("Error: Unknown algorithm '" + algorithm + "'");
            printUsage();
            return;
        }

        System.out.println("=== TRUCK LOADER ===");
        System.out.println("File: " + filePath);
        System.out.println("Algorithm: " + algorithm);
        System.out.println("===================\n");

        ParcelFileReader reader = new ParcelFileReader();

        try {
            List<Parcel> parcels = reader.readFromFile(filePath);
            System.out.println("\n✅ File successfully loaded!");
            reader.printParcels(parcels);

            TruckLoader loader = new TruckLoader();

            System.out.println("\n\n=== LOADING WITH " + algorithm.toUpperCase() + " ALGORITHM ===");
            List<Truck> trucks = loader.loadParcels(parcels, algorithm);

            loader.printTrucks(trucks);

        } catch (IOException e) {
            System.err.println("\n❌ Error reading file: " + e.getMessage());
            System.err.println("Check path: " + filePath);
        } catch (IllegalArgumentException e) {
            System.err.println("\n❌ Error: " + e.getMessage());
        }
    }

    private static boolean isValidAlgorithm(String algorithm) {
        return algorithm.equalsIgnoreCase("simple") ||
                algorithm.equalsIgnoreCase("maxdense");
    }

    private static void printUsage() {
        System.out.println("=== TRUCK LOADER USAGE ===");
        System.out.println("java ru.hofftech.Main <file-path> <algorithm>");
        System.out.println();
        System.out.println("Arguments:");
        System.out.println("  file-path   - path to file with parcels");
        System.out.println("  algorithm   - loading algorithm: simple | maxdense");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java ru.hofftech.Main test.txt simple");
        System.out.println("  java ru.hofftech.Main test.txt maxdense");
        System.out.println("=========================");
    }
}