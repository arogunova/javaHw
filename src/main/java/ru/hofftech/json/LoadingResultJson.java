package ru.hofftech.json;

import java.util.List;

/**
 * Корневая модель для JSON файла.
 * Хранит список машин.
 */
public class LoadingResultJson {
    private List<TruckJson> trucks;

    public LoadingResultJson(List<TruckJson> trucks) {
        this.trucks = trucks;
    }

    public List<TruckJson> getTrucks() {
        return trucks;
    }
}