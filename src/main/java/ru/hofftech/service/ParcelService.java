package ru.hofftech.service;

import ru.hofftech.model.Parcel;
import ru.hofftech.repository.ParcelRepository;

import java.util.List;

public class ParcelService {
    private final ParcelRepository repository;

    public ParcelService(ParcelRepository repository) {
        this.repository = repository;
    }

    public List<Parcel> getAllParcels() {
        return repository.findAll();
    }

    public Parcel getParcelByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Parcel not found: " + name
                ));
    }

    public void createParcel(String name, List<String> shape, char symbol) {
        Parcel parcel = new Parcel(name, shape, symbol);
        repository.add(parcel);
    }

    public void deleteParcel(String name) {
        repository.delete(name);
    }
}