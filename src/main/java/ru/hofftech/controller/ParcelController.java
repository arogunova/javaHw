package ru.hofftech.controller;

import ru.hofftech.model.Parcel;
import ru.hofftech.service.ParcelJpaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parcels")
public class ParcelController {

    private final ParcelJpaService parcelJpaService;

    public ParcelController(ParcelJpaService parcelJpaService) {
        this.parcelJpaService = parcelJpaService;
    }

    /**
     * GET /api/parcels?page=0&size=3
     */
    @GetMapping
    public Page<Parcel> getAllParcels(@PageableDefault(size = 10) Pageable pageable) {
        return parcelJpaService.getAllParcels(pageable);
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
        parcelJpaService.deleteParcel(name);
    }
}