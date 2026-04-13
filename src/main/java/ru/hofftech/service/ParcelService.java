package ru.hofftech.service;

import ru.hofftech.model.Parcel;
import ru.hofftech.repository.ParcelRepository;

import java.util.List;

/**
 * Сервис для работы с посылками.
 * Предоставляет бизнес-логику для операций CRUD.
 */
public class ParcelService {
    private final ParcelRepository repository;

    public ParcelService(ParcelRepository repository) {
        this.repository = repository;
    }

    /**
     * Возвращает список всех посылок.
     *
     * @return список всех посылок
     */
    public List<Parcel> getAllParcels() {
        return repository.findAll();
    }

    /**
     * Находит посылку по уникальному имени.
     *
     * @param name имя посылки
     * @return найденная посылка
     * @throws IllegalArgumentException если посылка не найдена
     */
    public Parcel getParcelByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Parcel not found: " + name
                ));
    }

    /**
     * Создаёт новую посылку.
     *
     * @param name уникальное имя посылки
     * @param shape форма посылки
     * @param symbol символ для отображения
     * @throws IllegalArgumentException если посылка с таким именем уже существует
     */
    public void createParcel(String name, List<String> shape, char symbol) {
        Parcel parcel = new Parcel(name, shape, symbol);
        repository.add(parcel);
    }

    /**
     * Удаляет посылку по имени.
     *
     * @param name имя посылки
     * @throws IllegalArgumentException если посылка не найдена
     */
    public void deleteParcel(String name) {
        repository.delete(name);
    }
}