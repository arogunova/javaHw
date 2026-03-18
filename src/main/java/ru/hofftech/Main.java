package ru.hofftech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hofftech.model.Parcel;
import ru.hofftech.file.ParcelFileReader;
import ru.hofftech.service.LoadingException;
import ru.hofftech.service.TruckLoader;
import ru.hofftech.model.Truck;

import java.io.IOException;
import java.util.List;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws LoadingException {
        if (args.length < 2) {
            log.error("Insufficient arguments provided");
            printUsage();
            return;
        }

        String filePath = args[0];
        String algorithm = args[1];

        if (!isValidAlgorithm(algorithm)) {
            log.error("Unknown algorithm: {}", algorithm);
            printUsage();
            return;
        }

        log.info("=== TRUCK LOADER ===");
        log.info("File: {}", filePath);
        log.info("Algorithm: {}", algorithm);
        log.info("===================");

        ParcelFileReader reader = new ParcelFileReader();

        try {
            List<Parcel> parcels = reader.readFromFile(filePath);
            log.info("✅ File successfully loaded!");
            reader.printParcels(parcels);

            TruckLoader loader = new TruckLoader();

            log.info("=== LOADING WITH {} ALGORITHM ===", algorithm.toUpperCase());
            List<Truck> trucks = loader.loadParcels(parcels, algorithm);

            loader.printTrucks(trucks);

        } catch (IOException e) {
            log.error("Error reading file: {}", e.getMessage());
            log.error("Check path: {}", filePath);
        } catch (IllegalArgumentException e) {
            log.error("Error: {}", e.getMessage());
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