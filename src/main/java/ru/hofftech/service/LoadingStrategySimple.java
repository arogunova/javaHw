package ru.hofftech.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hofftech.model.Parcel;
import ru.hofftech.model.PlacementResult;
import ru.hofftech.model.Truck;

import java.util.ArrayList;
import java.util.List;

public class LoadingStrategySimple implements LoadingStrategy {
    private static final Logger log = LoggerFactory.getLogger(LoadingStrategySimple.class);

    @Override
    public List<Truck> load(List<Parcel> parcels, int maxTrucks) throws LoadingException {
        log.info("--- USING SIMPLE LOADING STRATEGY ---");
        log.info("Each parcel will get its own truck");

        List<Truck> trucks = new ArrayList<>();

        for (int i = 0; i < parcels.size(); i++) {
            Parcel parcel = parcels.get(i);
            log.info("Processing parcel {}: {}", (i + 1), parcel);

            if (trucks.size() >= maxTrucks) {
                throw new LoadingException("Need more than " + maxTrucks + " trucks");
            }

            Truck truck = new Truck();
            log.debug("  Created new truck #{}", (trucks.size() + 1));

            PlacementResult result = truck.findPositionSimple(parcel);

            if (result.isFound()) {
                truck.placePackage(parcel, result);
                log.info("  Placed parcel at ({},{})", result.getX(), result.getY());

                trucks.add(truck);
            } else {
                log.error("  Cannot place parcel even in empty truck!");
            }
        }

        log.info("--- SIMPLE STRATEGY FINISHED ---");
        log.info("Trucks used: {}", trucks.size());

        return trucks;
    }
}