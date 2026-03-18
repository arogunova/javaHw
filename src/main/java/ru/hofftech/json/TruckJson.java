package ru.hofftech.json;

import java.util.List;

public class TruckJson {
    private List<ParcelJson> parcels;

    @SuppressWarnings("unused")
    public TruckJson() {
    }

    public TruckJson(List<ParcelJson> parcels) {
        this.parcels = parcels;
    }

    public List<ParcelJson> getParcels() {
        return parcels;
    }

    @SuppressWarnings("unused")
    public void setParcels(List<ParcelJson> parcels) {
        this.parcels = parcels;
    }
}