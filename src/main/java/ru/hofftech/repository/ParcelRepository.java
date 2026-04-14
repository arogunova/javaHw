package ru.hofftech.repository;

import ru.hofftech.model.Parcel;

import java.util.*;

/**
 * Репозиторий для хранения посылок.
 * Работает как in-memory база данных. Все посылки хранятся в Map: имя → посылка.
 */
public class ParcelRepository {

    private final Map<String, Parcel> parcels = new HashMap<>();

    /**
     * Конструктор. При создании добавляет стартовые посылки.
     */
    public ParcelRepository() {
        initDefaultParcels();
    }

    /**
     * Добавляет стартовые посылки из ДЗ1 с уникальными именами.
     */
    private void initDefaultParcels() {
        add(new Parcel("Посылка_тип_1", List.of("1"), '1'));
        add(new Parcel("Посылка_тип_2", List.of("22", "22"), '2'));
        add(new Parcel("Посылка_тип_3", List.of("333", "333", "333"), '3'));
        add(new Parcel("Посылка_тип_4", List.of("4444", "4444"), '4'));
        add(new Parcel("Посылка_тип_5", List.of("55555"), '5'));
        add(new Parcel("Посылка_тип_6", List.of("666", "666"), '6'));
        add(new Parcel("Посылка_тип_7", List.of("777", "7777"), '7'));
        add(new Parcel("Посылка_тип_8", List.of("8888", "8888"), '8'));
        add(new Parcel("Посылка_тип_9", List.of("999", "999", "999"), '9'));
    }

    /**
     * Находит посылку по имени.
     *
     * @param name имя посылки
     * @return Optional с посылкой или пустой Optional, если не найдена
     */
    public Optional<Parcel> findByName(String name) {
        return Optional.ofNullable(parcels.get(name));
    }

    /**
     * Возвращает список всех посылок в репозитории.
     *
     * @return список всех посылок
     */
    public List<Parcel> findAll() {
        return new ArrayList<>(parcels.values());
    }

    /**
     * Добавляет новую посылку.
     *
     * @param parcel посылка для добавления
     * @throws IllegalArgumentException если посылка с таким именем уже существует
     */
    public void add(Parcel parcel) {
        if (parcels.containsKey(parcel.getName())) {
            throw new IllegalArgumentException(
                    "Parcel with name '" + parcel.getName() + "' already exists"
            );
        }
        parcels.put(parcel.getName(), parcel);
    }

    /**
     * Удаляет посылку по имени.
     *
     * @param name имя посылки
     * @throws IllegalArgumentException если посылка не найдена
     */
    public void delete(String name) {
        if (!parcels.containsKey(name)) {
            throw new IllegalArgumentException(
                    "Parcel with name '" + name + "' not found"
            );
        }
        parcels.remove(name);
    }
}