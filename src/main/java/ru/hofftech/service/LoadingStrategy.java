package ru.hofftech.service;

import ru.hofftech.model.Parcel;
import ru.hofftech.model.Truck;

import java.util.List;

/**
 * Интерфейс стратегии загрузки посылок в машины.
 * Каждая реализация определяет свой алгоритм упаковки.
 */
public interface LoadingStrategy {

    /**
     * Загружает посылки в машины согласно алгоритму стратегии.
     *
     * @param parcels список посылок для загрузки
     * @param maxTrucks максимально допустимое количество машин
     * @return список загруженных машин
     * @throws LoadingException если загрузка невозможна (например, превышен maxTrucks)
     */
    List<Truck> load(List<Parcel> parcels, int maxTrucks);
}