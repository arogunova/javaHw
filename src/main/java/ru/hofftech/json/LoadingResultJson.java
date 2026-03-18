package ru.hofftech.json;

import java.util.List;

public class LoadingResultJson {
    private List<TruckJson> trucks;

    @SuppressWarnings("unused")
    public LoadingResultJson() {
    }

    public LoadingResultJson(List<TruckJson> trucks) {
        this.trucks = trucks;
    }

    public List<TruckJson> getTrucks() {
        return trucks;
    }

    @SuppressWarnings("unused")
    public void setTrucks(List<TruckJson> trucks) {
        this.trucks = trucks;
    }
}