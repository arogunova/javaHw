package ru.hofftech.json;

import java.util.List;

/**
 * Корневая модель для JSON файла.
 * Содержит список всех загруженных машин.
 */
public class LoadingResultJson {
    private List<TruckJson> trucks;

    /**
     * Пустой конструктор для Jackson (требуется для десериализации).
     */
    @SuppressWarnings("unused")
    public LoadingResultJson() {
    }

    /**
     * Конструктор для создания корневой модели.
     *
     * @param trucks список машин
     */
    public LoadingResultJson(List<TruckJson> trucks) {
        this.trucks = trucks;
    }

    /**
     * Возвращает список машин.
     *
     * @return список машин
     */
    public List<TruckJson> getTrucks() {
        return trucks;
    }

    /**
     * Устанавливает список машин.
     *
     * @param trucks список машин
     */
    @SuppressWarnings("unused")
    public void setTrucks(List<TruckJson> trucks) {
        this.trucks = trucks;
    }
}