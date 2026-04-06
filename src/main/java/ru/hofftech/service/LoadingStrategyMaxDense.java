package ru.hofftech.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hofftech.model.Parcel;
import ru.hofftech.model.PlacementResult;
import ru.hofftech.model.Truck;

import java.util.ArrayList;
import java.util.List;

public class LoadingStrategyMaxDense implements LoadingStrategy {
    private static final Logger log = LoggerFactory.getLogger(LoadingStrategyMaxDense.class);

    @Override
    public List<Truck> load(List<Parcel> parcels, int maxTrucks) {
        log.info("--- USING MAX DENSE LOADING STRATEGY ---");
        log.info("Trying to pack parcels as tightly as possible");

        List<Parcel> sortedParcels = new ArrayList<>(parcels);
        sortedParcels.sort((p1, p2) -> Integer.compare(p2.getArea(), p1.getArea()));

        log.debug("Parcels sorted by size (largest first):");
        for (Parcel p : sortedParcels) {
            log.debug("  {}", p);
        }

        List<Truck> trucks = new ArrayList<>();

        for (int i = 0; i < sortedParcels.size(); i++) {
            Parcel parcel = sortedParcels.get(i);
            log.info("Processing parcel {}: {}", (i + 1), parcel);

            boolean placed = false;

            for (int t = 0; t < trucks.size(); t++) {
                Truck truck = trucks.get(t);
                log.debug("  Trying existing truck #{}", (t + 1));

                PlacementResult result = truck.findPositionSimple(parcel);

                if (result.isFound()) {
                    truck.placePackage(parcel, result);
                    log.info("    Placed in existing truck at ({},{})", result.getX(), result.getY());
                    placed = true;
                    break;
                } else {
                    log.debug("    No space in this truck");
                }
            }

            if (!placed) {
                log.info("  No space in existing trucks, creating new truck #{}", (trucks.size() + 1));

                if (trucks.size() >= maxTrucks) {
                    throw new LoadingException("Need more than " + maxTrucks + " trucks");
                }

                Truck newTruck = new Truck();
                PlacementResult result = newTruck.findPositionSimple(parcel);

                if (result.isFound()) {
                    newTruck.placePackage(parcel, result);
                    log.info("    Placed in new truck at ({},{})", result.getX(), result.getY());
                    trucks.add(newTruck);
                } else {
                    log.error("    Cannot place parcel even in empty truck!");
                    throw new LoadingException("Cannot place parcel " + parcel.getSymbol() + " in empty truck");
                }
            }
        }

        log.info("--- MAX DENSE STRATEGY FINISHED ---");
        log.info("Trucks used: {}", trucks.size());

        return trucks;
    }
}