package ru.hofftech.file;

import ru.hofftech.model.Parcel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ParcelFileReader {

    public List<Parcel> readFromFile(String filePath) throws IOException {
        List<String> allLines = Files.readAllLines(Paths.get(filePath));
        List<Parcel> parcels = new ArrayList<>();
        List<String> currentParcelLines = new ArrayList<>();

        for (String line : allLines) {
            if (line.trim().isEmpty()) {
                if (!currentParcelLines.isEmpty()) {
                    parcels.add(createParcel(currentParcelLines));
                    currentParcelLines = new ArrayList<>();
                }
            } else {
                currentParcelLines.add(line);
            }
        }

        if (!currentParcelLines.isEmpty()) {
            parcels.add(createParcel(currentParcelLines));
        }

        return parcels;
    }

    private Parcel createParcel(List<String> lines) {
        char symbol = '?';
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                symbol = line.charAt(0);
                break;
            }
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