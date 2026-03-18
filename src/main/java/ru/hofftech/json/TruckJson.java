package ru.hofftech.json;

import java.util.List;

/**
 * Модель машины для JSON.
 * Хранит список посылок.
 */
public class TruckJson {
    private final List<ParcelJson> parcels;

    public TruckJson(List<ParcelJson> parcels) {
        this.parcels = parcels;
    }

    public List<ParcelJson> getParcels() {
        return parcels;
    }
}