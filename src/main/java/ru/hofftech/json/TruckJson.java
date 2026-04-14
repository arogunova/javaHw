package ru.hofftech.json;

import java.util.List;

/**
 * Модель машины для JSON.
 * Содержит список посылок, размещённых в этой машине.
 */
public class TruckJson {
    private List<ParcelJson> parcels;

    /**
     * Пустой конструктор для Jackson (требуется для десериализации).
     */
    @SuppressWarnings("unused")
    public TruckJson() {
    }

    /**
     * Конструктор для создания JSON-модели машины.
     *
     * @param parcels список посылок в машине
     */
    public TruckJson(List<ParcelJson> parcels) {
        this.parcels = parcels;
    }

    /**
     * Возвращает список посылок в машине.
     *
     * @return список посылок
     */
    public List<ParcelJson> getParcels() {
        return parcels;
    }

    /**
     * Устанавливает список посылок в машине.
     *
     * @param parcels список посылок
     */
    @SuppressWarnings("unused")
    public void setParcels(List<ParcelJson> parcels) {
        this.parcels = parcels;
    }
}