package ru.hofftech.model;

public class PlacementResult {
    private final boolean found;  // true если место найдено
    private final int x;          // координата X (если найдено)
    private final int y;          // координата Y (если найдено)

    public PlacementResult(boolean found, int x, int y) {
        this.found = found;
        this.x = x;
        this.y = y;
    }

    public PlacementResult(boolean found) {
        this(found, 0, 0);
    }

    public boolean isFound() {
        return found;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        if (found) {
            return "Place found at (" + x + "," + y + ")";
        } else {
            return "No place found";
        }
    }
}