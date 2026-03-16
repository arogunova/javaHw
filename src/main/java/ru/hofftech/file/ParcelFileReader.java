package ru.hofftech.file;

import ru.hofftech.model.Parcel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ParcelFileReader {

    public List<Parcel> readFromFile(String filePath) throws IOException {
        System.out.println("Reading file: " + filePath);

        List<String> allLines = Files.readAllLines(Paths.get(filePath));

        List<Parcel> parcels = new ArrayList<>();

        List<String> currentParcelLines = new ArrayList<>();

        for (String line : allLines) {
            if (line.trim().isEmpty()) {
                if (!currentParcelLines.isEmpty()) {
                    Parcel parcel = createParcel(currentParcelLines);
                    parcels.add(parcel);

                    currentParcelLines = new ArrayList<>();

                    System.out.println("Found parcel with symbol: " + parcel.getSymbol()); 
                }
            } else {
                currentParcelLines.add(line);
            }
        }

        if (!currentParcelLines.isEmpty()) {
            Parcel parcel = createParcel(currentParcelLines);
            parcels.add(parcel);
            System.out.println("Found last parcel with symbol: " + parcel.getSymbol()); 
        }

        System.out.println("Total parcels found: " + parcels.size()); 
        return parcels;
    }

    private Parcel createParcel(List<String> lines) {
        char symbol = '?';
        for (String line : lines) {
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (!Character.isWhitespace(c)) {
                    symbol = c;
                    break;
                }
            }
            if (symbol != '?') break;
        }

        if (symbol == '?') {
            throw new IllegalArgumentException("Parcel has no visible symbols");
        }

        return new Parcel(lines, symbol);
    }

    public void printParcels(List<Parcel> parcels) {
        System.out.println("Total parcels: " + parcels.size());
        System.out.println("-------------------");

        for (int i = 0; i < parcels.size(); i++) {
            Parcel parcel = parcels.get(i);

            System.out.println("\nParcel #" + (i + 1));
            System.out.println("Info: " + parcel);
            System.out.println("Shape:");

            for (String line : parcel.getShape()) {
                System.out.println("  " + line);
            }
        }
    }
}