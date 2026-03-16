package ru.hofftech.model;

// Импортируем List для работы со списком строк
import java.util.List;

/**
 * Класс Parcel (Посылка) - модель данных, представляющая одну посылку.

 * Этот класс хранит информацию о посылке:
 * - shape - форма посылки (список строк)
 * - symbol - символ, из которого состоит посылка
 * - width - ширина посылки
 * - height - высота посылки
 * - area - площадь (сколько клеток занимает)

 * Класс является immutable (неизменяемым) - после создания объекта
 * изменить его поля нельзя (все поля final).
 */
public class Parcel {
    // =============== ПОЛЯ КЛАССА ===============

    /**
     * shape - форма посылки в виде списка строк.
     * Каждая строка - это один ряд посылки.
     * Например, для посылки вида:
     *   999
     *   999
     *   999
     * shape будет: ["999", "999", "999"]
     */
    private final List<String> shape;

    /**
     * symbol - символ, которым нарисована посылка.
     * В примере выше это '9'.
     * Может быть '1', '2', '3', '4', '5', '6', '7', '8', '9'
     */
    private final char symbol;

    /**
     * width - ширина посылки.
     * Вычисляется как максимальная длина строки в shape.
     * Для посылки 3x3 ширина = 3
     */
    private final int width;

    /**
     * height - высота посылки.
     * Вычисляется как количество строк в shape.
     * Для посылки 3x3 высота = 3
     */
    private final int height;

    /**
     * area - площадь посылки.
     * Сколько клеток занимает посылка (количество символов symbol).
     * Для посылки 3x3, заполненной полностью, area = 9
     */
    private final int area;

    // =============== КОНСТРУКТОР ===============

    /**
     * Конструктор - создает новый объект посылки.
     *
     * @param shape форма посылки (список строк)
     * @param symbol символ посылки
     */
    public Parcel(List<String> shape, char symbol) {
        // Проверяем входные данные
        validateInput(shape, symbol);

        // Сохраняем переданные значения в поля класса
        this.shape = shape;
        this.symbol = symbol;

        // Вычисляем характеристики посылки
        this.height = calculateHeight(shape);
        this.width = calculateWidth(shape);
        this.area = calculateArea(shape, symbol);
    }

    // =============== ПРИВАТНЫЕ МЕТОДЫ ДЛЯ ВЫЧИСЛЕНИЙ ===============

    /**
     * Проверяет корректность входных данных.
     *
     * @param shape форма посылки
     * @param symbol символ посылки
     * @throws IllegalArgumentException если данные некорректны
     */
    private void validateInput(List<String> shape, char symbol) {
        if (shape == null) {
            throw new IllegalArgumentException("Shape cannot be null");
        }
        if (shape.isEmpty()) {
            throw new IllegalArgumentException("Shape cannot be empty");
        }
        if (symbol == ' ') {
            throw new IllegalArgumentException("Symbol cannot be space");
        }
        // Проверяем, что все строки содержат только символ посылки или пробелы
        for (String line : shape) {
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (c != symbol && c != ' ') {
                    throw new IllegalArgumentException(
                            "Shape contains invalid character '" + c + "'. Expected '" + symbol + "' or space"
                    );
                }
            }
        }
    }

    /**
     * Вычисляет высоту посылки.
     *
     * @param shape форма посылки
     * @return количество строк
     */
    private int calculateHeight(List<String> shape) {
        return shape.size();
    }

    /**
     * Вычисляет ширину посылки (максимальная длина строки).
     *
     * @param shape форма посылки
     * @return максимальная длина строки
     */
    private int calculateWidth(List<String> shape) {
        int maxWidth = 0;
        for (String line : shape) {
            if (line.length() > maxWidth) {
                maxWidth = line.length();
            }
        }
        return maxWidth;
    }

    /**
     * Вычисляет площадь посылки (количество символов symbol).
     *
     * @param shape форма посылки
     * @param symbol символ посылки
     * @return количество клеток, которые занимает посылка
     */
    private int calculateArea(List<String> shape, char symbol) {
        int areaCount = 0;
        for (String line : shape) {
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == symbol) {
                    areaCount++;
                }
            }
        }
        return areaCount;
    }

    // =============== ГЕТТЕРЫ ===============

    /**
     * Возвращает форму посылки.
     * @return список строк с формой посылки
     */
    public List<String> getShape() {
        return shape;
    }

    /**
     * Возвращает символ посылки.
     * @return символ, которым нарисована посылка
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Возвращает ширину посылки.
     * @return ширина в клетках
     */
    public int getWidth() {
        return width;
    }

    /**
     * Возвращает высоту посылки.
     * @return высота в клетках
     */
    public int getHeight() {
        return height;
    }

    /**
     * Возвращает площадь посылки.
     * @return количество клеток, которые занимает посылка
     */
    public int getArea() {
        return area;
    }

    // =============== МЕТОДЫ ДЛЯ УДОБСТВА ===============

    /**
     * Переопределяем метод toString().
     *
     * @return строковое представление посылки
     */
    @Override
    public String toString() {
        return String.format("Parcel '%c': %dx%d, area=%d",
                symbol, width, height, area);
    }
}