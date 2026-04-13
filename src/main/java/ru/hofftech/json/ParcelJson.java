package ru.hofftech.json;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Модель посылки для JSON.
 * Хранит имя, форму и координаты размещения.
 */
public class ParcelJson {
    private String name;
    private List<String> shape;
    private List<Integer> pos;  // [x, y]

    /**
     * Пустой конструктор для Jackson (требуется для десериализации).
     */
    @SuppressWarnings("unused")
    public ParcelJson() {
    }

    /**
     * Конструктор для создания JSON-модели посылки.
     *
     * @param name имя посылки
     * @param shape форма посылки
     * @param x координата X левого нижнего угла
     * @param y координата Y левого нижнего угла
     */
    public ParcelJson(String name, List<String> shape, int x, int y) {
        this.name = name;
        this.shape = shape;
        this.pos = List.of(x, y);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<String> getShape() { return shape; }

    @SuppressWarnings("unused")
    public void setShape(List<String> shape) { this.shape = shape; }

    @SuppressWarnings("unused")
    public List<Integer> getPos() { return pos; }

    @SuppressWarnings("unused")
    public void setPos(List<Integer> pos) { this.pos = pos; }

    /**
     * Возвращает координату X.
     * Jackson игнорирует этот метод при сериализации.
     *
     * @return координата X
     */
    @JsonIgnore
    public int getX() {
        return pos.getFirst();
    }

    /**
     * Возвращает координату Y.
     * Jackson игнорирует этот метод при сериализации.
     *
     * @return координата Y
     */
    @JsonIgnore
    public int getY() {
        return pos.get(1);
    }
}