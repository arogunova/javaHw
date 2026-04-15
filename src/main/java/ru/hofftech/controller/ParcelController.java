package ru.hofftech.controller;

import org.springframework.http.ResponseEntity;
import ru.hofftech.model.Parcel;
import ru.hofftech.model.Truck;
import ru.hofftech.service.ParcelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/parcels")
public class ParcelController {

    private final ParcelService parcelService;

    public ParcelController(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    /**
     * GET /api/parcels?page=0&size=3
     */
    @GetMapping
    public Page<Parcel> getAllParcels(@PageableDefault() Pageable pageable) {
        return parcelService.getAllParcels(pageable);
    }

    @GetMapping("/{name}")
    public Parcel getParcelByName(@PathVariable String name) {
        return parcelService.getParcelByName(name);
    }

    @PostMapping
    public Parcel createParcel(@RequestBody CreateParcelRequest request) {
        parcelService.createParcel(request.getName(), request.getShape(), request.getSymbol());
        return parcelService.getParcelByName(request.getName());
    }

    @DeleteMapping("/{name}")
    public void deleteParcel(@PathVariable String name) {
        parcelService.deleteParcel(name);
    }

    @PostMapping("/load")
    public ResponseEntity<?> loadParcels(@RequestBody LoadRequest request) {
        try {
            List<Truck> trucks = parcelService.loadParcels(
                    request.getParcelNames(),
                    request.getAlgorithm(),
                    request.getMaxTrucks()
            );
            return ResponseEntity.ok(trucks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/unload")
    public ResponseEntity<?> unloadTrucks(@RequestBody List<Map<String, Object>> trucksData) {
        try {
            List<Truck> trucks = new ArrayList<>();

            for (Map<String, Object> truckData : trucksData) {
                Object packagesInfoObj = truckData.get("packagesInfo");
                if (!(packagesInfoObj instanceof List<?> packagesInfoRaw)) {
                    throw new IllegalArgumentException("packagesInfo must be a list");
                }

                Truck truck = new Truck();

                for (Object item : packagesInfoRaw) {
                    if (!(item instanceof List<?> info)) {
                        throw new IllegalArgumentException("Each package info must be a list");
                    }

                    if (info.size() < 3) {
                        throw new IllegalArgumentException("Invalid package info format");
                    }

                    // Получаем данные посылки
                    Map<String, Object> parcelMap = (Map<String, Object>) info.getFirst();
                    String name = (String) parcelMap.get("name");

                    // shape может быть List или String
                    Object shapeObj = parcelMap.get("shape");
                    List<String> shape;
                    if (shapeObj instanceof List) {
                        shape = (List<String>) shapeObj;
                    } else {
                        throw new IllegalArgumentException("shape must be a list");
                    }

                    String symbolStr = (String) parcelMap.get("symbol");
                    char symbol = symbolStr.charAt(0);

                    // Координаты
                    Number xNum = (Number) info.get(1);
                    Number yNum = (Number) info.get(2);
                    int x = xNum.intValue();
                    int y = yNum.intValue();

                    Parcel parcel = new Parcel(name, shape, symbol);
                    truck.placePackageAt(parcel, x, y);
                }
                trucks.add(truck);
            }

            List<String> parcelNames = parcelService.unloadTrucks(trucks);
            return ResponseEntity.ok(Map.of("parcels", parcelNames));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}