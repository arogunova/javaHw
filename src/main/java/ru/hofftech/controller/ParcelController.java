package ru.hofftech.controller;

import ru.hofftech.model.Parcel;
import ru.hofftech.service.ParcelJpaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parcels")
public class ParcelController {

    private final ParcelJpaService parcelJpaService;

    public ParcelController(ParcelJpaService parcelJpaService) {
        this.parcelJpaService = parcelJpaService;
    }

    @GetMapping
    public List<Parcel> getAllParcels() {
        return parcelJpaService.getAllParcels();
    }

    @GetMapping("/{name}")
    public Parcel getParcelByName(@PathVariable String name) {
        return parcelJpaService.getParcelByName(name);
    }

    @PostMapping
    public Parcel createParcel(@RequestBody CreateParcelRequest request) {
        parcelJpaService.createParcel(request.getName(), request.getShape(), request.getSymbol());
        return parcelJpaService.getParcelByName(request.getName());
    }

    @DeleteMapping("/{name}")
    public void deleteParcel(@PathVariable String name) {
        parcelJpaService.deleteParcel(name);  // ← исправлено
    }
}