package ru.hofftech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hofftech.model.Parcel;
import ru.hofftech.file.ParcelFileReader;
import ru.hofftech.service.LoadingException;
import ru.hofftech.service.TruckLoader;
import ru.hofftech.model.Truck;
import ru.hofftech.json.JsonFileService;

import java.io.IOException;
import java.util.List;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws LoadingException {
        if (args.length == 0) {
            printUsage();
            return;
        }
        System.out.println("Received " + args.length + " arguments:");
        for (int i = 0; i < args.length; i++) {
            System.out.println("  args[" + i + "] = '" + args[i] + "'");
        }

        if (args[0].equals("--load")) {
            if (args.length < 2) {
                System.err.println("Error: Missing JSON file path");
                return;
            }
            String jsonFile = args[1];
            log.info("Loading trucks from JSON: {}", jsonFile);

            try {
                List<Truck> trucks = JsonFileService.loadFromFile(jsonFile);

                TruckLoader loader = new TruckLoader();
                loader.printTrucks(trucks);
            } catch (Exception e) {
                log.error("Error loading JSON: {}", e.getMessage());
            }
            return;
        }

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

            if (args.length > 2 && args[2].equals("--save")) {
                if (args.length < 4) {
                    log.error("Missing output file for --save");
                } else {
                    String jsonFile = args[3];
                    log.info("Saving result to JSON: {}", jsonFile);
                    JsonFileService.saveToFile(trucks, jsonFile);
                    System.out.println("✅ Saved to " + jsonFile);
                }
            }
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
        System.out.println("1. Load parcels from text file and pack:");
        System.out.println("   java ru.hofftech.Main <file-path> <algorithm> <max-trucks> [--save <json-file>]");
        System.out.println();
        System.out.println("2. Load already packed trucks from JSON:");
        System.out.println("   java ru.hofftech.Main --load <json-file>");
        System.out.println();
        System.out.println("Arguments:");
        System.out.println("  file-path   - path to file with parcels (.txt)");
        System.out.println("  algorithm   - loading algorithm: simple | maxdense");
        System.out.println("  max-trucks  - maximum number of trucks allowed");
        System.out.println("  --save      - save result to JSON file");
        System.out.println("  --load      - load result from JSON file");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java ru.hofftech.Main test.txt maxdense 3");
        System.out.println("  java ru.hofftech.Main test.txt simple 2 --save result.json");
        System.out.println("  java ru.hofftech.Main --load result.json");
        System.out.println("=========================");
    }
}