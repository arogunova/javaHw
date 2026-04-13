package ru.hofftech.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class Parcel {
    private static final Logger log = LoggerFactory.getLogger(Parcel.class);

    private final String name;
    private final List<String> shape;
    private final char symbol;
    private final int width;
    private final int height;
    private final int area;

    public Parcel(String name, List<String> shape, char symbol) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (name.contains(" ")) {
            throw new IllegalArgumentException("Name cannot contain spaces. Use underscore (_) instead");
        }
        log.debug("Creating parcel with symbol: {}, shape size: {}", symbol, shape.size());
        validateInput(shape, symbol);
        this.name = name;
        this.shape = shape;
        this.symbol = symbol;
        this.height = calculateHeight(shape);
        this.width = calculateWidth(shape);
        this.area = calculateArea(shape, symbol);
        log.debug("Parcel created: {}", this);
    }

    private void validateInput(List<String> shape, char symbol) {
        if (shape == null) {
            log.error("Shape cannot be null");
            throw new IllegalArgumentException("Shape cannot be null");
        }
        if (shape.isEmpty()) {
            log.error("Shape cannot be empty");
            throw new IllegalArgumentException("Shape cannot be empty");
        }
        if (symbol == ' ') {
            log.error("Symbol cannot be space");
            throw new IllegalArgumentException("Symbol cannot be space");
        }
        log.debug("Input validation passed for symbol: {}", symbol);
    }

    private int calculateHeight(List<String> shape) {
        int height = shape.size();
        log.debug("Calculated height: {}", height);
        return height;
    }

    private int calculateWidth(List<String> shape) {
        int maxWidth = 0;
        for (String line : shape) {
            if (line.length() > maxWidth) {
                maxWidth = line.length();
            }
        }
        log.debug("Calculated width: {}", maxWidth);
        return maxWidth;
    }

    private int calculateArea(List<String> shape, char symbol) {
        int areaCount = 0;
        for (String line : shape) {
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == symbol) {
                    areaCount++;
                }
            }
        }
        log.debug("Calculated area: {}", areaCount);
        return areaCount;
    }

    public List<String> getShape() {
        return shape;
    }

    public char getSymbol() {
        return symbol;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getArea() {
        return area;
    }

    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return String.format("Parcel '%s': %c %dx%d, area=%d",
                name, symbol, width, height, area);
    }
}