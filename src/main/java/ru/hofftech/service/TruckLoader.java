package ru.hofftech.service;

import ru.hofftech.model.Parcel;
import ru.hofftech.model.Truck;
import ru.hofftech.model.PlacementResult;

import java.util.ArrayList;
import java.util.List;

public class TruckLoader {

    public List<Truck> loadParcels(List<Parcel> parcels, boolean useOptimization) {
        System.out.println("\n=== STARTING TRUCK LOADING ===");
        System.out.println("Total parcels to load: " + parcels.size());
        System.out.println("Algorithm: " + (useOptimization ? "OPTIMAL" : "SIMPLE"));

        if (useOptimization) {
            return loadOptimally(parcels);
        } else {
            return loadSimply(parcels);
        }
    }

    private List<Truck> loadSimply(List<Parcel> parcels) {
        System.out.println("\n--- USING SIMPLE ALGORITHM ---");

        List<Truck> trucks = new ArrayList<>();

        for (Parcel parcel : parcels) {
            Truck truck = new Truck();
            PlacementResult result = truck.findPositionSimple(parcel);

            if (result.isFound()) {
                truck.placePackage(parcel, result);
                trucks.add(truck);
            }
        }

        System.out.println("\n--- SIMPLE ALGORITHM FINISHED ---");
        System.out.println("Trucks used: " + trucks.size());

        return trucks;
    }

    private List<Truck> loadOptimally(List<Parcel> parcels) {
        System.out.println("\n--- USING OPTIMAL ALGORITHM ---");

        List<Parcel> sortedParcels = new ArrayList<>(parcels);
        sortedParcels.sort((p1, p2) -> Integer.compare(p2.getArea(), p1.getArea()));

        List<Truck> trucks = new ArrayList<>();

        for (Parcel parcel : sortedParcels) {
            boolean placed = false;

            for (Truck truck : trucks) {
                PlacementResult result = truck.findPositionSimple(parcel);
                if (result.isFound()) {
                    truck.placePackage(parcel, result);
                    placed = true;
                    break;
                }
            }

            if (!placed) {
                Truck newTruck = new Truck();
                PlacementResult result = newTruck.findPositionSimple(parcel);
                if (result.isFound()) {
                    newTruck.placePackage(parcel, result);
                    trucks.add(newTruck);
                }
            }
        }

        System.out.println("\n--- OPTIMAL ALGORITHM FINISHED ---");
        System.out.println("Trucks used: " + trucks.size());

        return trucks;
    }

    public void printTrucks(List<Truck> trucks) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("LOADING RESULT");
        System.out.println("=".repeat(50));

        if (trucks == null || trucks.isEmpty()) {
            System.out.println("No trucks used");
            return;
        }

        System.out.println("Total trucks used: " + trucks.size());

        for (int i = 0; i < trucks.size(); i++) {
            System.out.println("\nTRUCK #" + (i + 1));
            System.out.println(trucks.get(i));
        }

        printStatistics(trucks);
    }

    private void printStatistics(List<Truck> trucks) {
        System.out.println("\n" + "-".repeat(30));
        System.out.println("STATISTICS");
        System.out.println("-".repeat(30));

        int totalTrucks = trucks.size();
        int totalParcels = 0;

        for (Truck truck : trucks) {
            totalParcels += truck.getPackagesCount();
        }

        System.out.println("Total trucks: " + totalTrucks);
        System.out.println("Total parcels: " + totalParcels);
    }
}