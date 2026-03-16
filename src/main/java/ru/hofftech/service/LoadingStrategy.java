package ru.hofftech.service;

import ru.hofftech.model.Parcel;
import ru.hofftech.model.Truck;

import java.util.List;

/**
 * Интерфейс стратегии загрузки посылок в машины.
 *
 * Каждая стратегия реализует свой алгоритм упаковки:
 * - SimpleLoadingStrategy: одна посылка - одна машина
 * - MaxDenseLoadingStrategy: максимально плотная упаковка
 *
 * В будущем можно добавлять новые стратегии, не меняя существующий код.
 */
public interface LoadingStrategy {

    /**
     * Загружает посылки в машины согласно алгоритму стратегии.
     *
     * @param parcels список посылок для загрузки
     * @return список загруженных машин
     */
    List<Truck> load(List<Parcel> parcels);
}