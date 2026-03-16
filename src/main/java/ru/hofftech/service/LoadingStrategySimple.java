package ru.hofftech.service;

import ru.hofftech.model.Parcel;
import ru.hofftech.model.PlacementResult;
import ru.hofftech.model.Truck;

import java.util.ArrayList;
import java.util.List;

public class LoadingStrategySimple implements LoadingStrategy {

    @Override
    public List<Truck> load(List<Parcel> parcels) {
        System.out.println("\n--- USING SIMPLE LOADING STRATEGY ---");
        System.out.println("Each parcel will get its own truck");

        List<Truck> trucks = new ArrayList<>();

        for (int i = 0; i < parcels.size(); i++) {
            Parcel parcel = parcels.get(i);
            System.out.println("\nProcessing parcel " + (i + 1) + ": " + parcel);

            Truck truck = new Truck();
            System.out.println("  Created new truck #" + (trucks.size() + 1));

            PlacementResult result = truck.findPositionSimple(parcel);

            if (result.isFound()) {
                truck.placePackage(parcel, result);
                System.out.println("  Placed parcel at (" + result.getX() + "," + result.getY() + ")");

                trucks.add(truck);
            } else {
                System.err.println("  ERROR: Cannot place parcel even in empty truck!");
            }
        }

        System.out.println("\n--- SIMPLE STRATEGY FINISHED ---");
        System.out.println("Trucks used: " + trucks.size());

        return trucks;
    }
}