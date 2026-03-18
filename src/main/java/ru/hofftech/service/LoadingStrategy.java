package ru.hofftech.service;

import ru.hofftech.model.Parcel;
import ru.hofftech.model.Truck;

import java.util.List;

public interface LoadingStrategy {
    List<Truck> load(List<Parcel> parcels, int maxTrucks) throws LoadingException;
}