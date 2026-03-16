package ru.hofftech.model;

import java.util.List;

public class Parcel {
    private final List<String> shape;
    private final char symbol;
    private final int width;
    private final int height;
    private final int area;

    public Parcel(List<String> shape, char symbol) {
        validateInput(shape, symbol);
        this.shape = shape;
        this.symbol = symbol;
        this.height = calculateHeight(shape);
        this.width = calculateWidth(shape);
        this.area = calculateArea(shape, symbol);
    }

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

    private int calculateHeight(List<String> shape) {
        return shape.size();
    }

    private int calculateWidth(List<String> shape) {
        int maxWidth = 0;
        for (String line : shape) {
            if (line.length() > maxWidth) {
                maxWidth = line.length();
            }
        }
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

    @Override
    public String toString() {
        return String.format("Parcel '%c': %dx%d, area=%d",
                symbol, width, height, area);
    }
}