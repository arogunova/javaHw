package ru.hofftech.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.hofftech.entity.ParcelEntity;
import ru.hofftech.model.Parcel;
import ru.hofftech.model.Truck;
import ru.hofftech.repository.ParcelRepository;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParcelService {

    private final ParcelRepository parcelRepository;

    public ParcelService(ParcelRepository parcelRepository) {
        this.parcelRepository = parcelRepository;
    }

    public Page<Parcel> getAllParcels(Pageable pageable) {
        return parcelRepository.findAll(pageable).map(this::toModel);
    }

    public List<Parcel> getAllParcelsList() {
        return parcelRepository.findAll().stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
    public Parcel getParcelByName(String name) {
        return parcelRepository.findByName(name)
                .map(this::toModel)
                .orElseThrow(() -> new IllegalArgumentException("Parcel not found: " + name));
    }

    public void createParcel(String name, List<String> shape, char symbol) {
        if (parcelRepository.existsByName(name)) {
            throw new IllegalArgumentException("Parcel with name '" + name + "' already exists");
        }

        int height = shape.size();
        int width = calculateWidth(shape);
        int area = calculateArea(shape, symbol);
        ParcelEntity entity = new ParcelEntity(name, symbol, shape, width, height, area);
        parcelRepository.save(entity);
    }

    public void deleteParcel(String name) {
        if (!parcelRepository.existsByName(name)) {
            throw new IllegalArgumentException("Parcel with name '" + name + "' not found");
        }
        parcelRepository.deleteByName(name);
    }

    public List<Truck> loadParcels(List<String> parcelNames, String algorithm, int maxTrucks) {
        List<Parcel> parcels = new ArrayList<>();
        for (String name : parcelNames) {
            Parcel parcel = getParcelByName(name);
            parcels.add(parcel);
        }
        TruckLoader loader = new TruckLoader();
        return loader.loadParcels(parcels, algorithm, maxTrucks);
    }

    public List<String> unloadTrucks(List<Truck> trucks) {
        List<String> parcelNames = new ArrayList<>();
        for (Truck truck : trucks) {
            List<Object[]> packagesInfo = truck.getPackagesInfo();
            for (Object[] info : packagesInfo) {
                Parcel parcel = (Parcel) info[0];
                parcelNames.add(parcel.getName());
            }
        }
        return parcelNames;
    }

    private int calculateWidth(List<String> shape) {
        int maxWidth = 0;
        for (String line : shape) {
            if (line.length() > maxWidth) {
                maxWidth = line.length();
            }
        }
        return maxWidth;
    }

    private int calculateArea(List<String> shape, char symbol) {
        int areaCount = 0;
        for (String line : shape) {
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == symbol) {
                    areaCount++;
                }
            }
        }
        return areaCount;
    }

    private Parcel toModel(ParcelEntity entity) {
        return new Parcel(entity.getName(), entity.getShape(), entity.getSymbol());
    }


}