package ru.hofftech.service;

import ru.hofftech.entity.ParcelEntity;
import ru.hofftech.model.Parcel;
import ru.hofftech.repository.ParcelJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParcelJpaService {

    private final ParcelJpaRepository parcelRepository;

    public ParcelJpaService(ParcelJpaRepository parcelRepository) {
        this.parcelRepository = parcelRepository;
    }

    public List<Parcel> getAllParcels() {
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
        // Убрали преобразование в строку — передаём List напрямую
        ParcelEntity entity = new ParcelEntity(name, symbol, shape, width, height, area);
        parcelRepository.save(entity);
    }

    public void deleteParcel(String name) {
        if (!parcelRepository.existsByName(name)) {
            throw new IllegalArgumentException("Parcel with name '" + name + "' not found");
        }
        parcelRepository.deleteByName(name);
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