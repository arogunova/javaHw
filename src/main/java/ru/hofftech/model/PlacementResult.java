package ru.hofftech.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlacementResult {
    private static final Logger log = LoggerFactory.getLogger(PlacementResult.class);

    private final boolean found;
    private final int x;
    private final int y;

    public PlacementResult(boolean found, int x, int y) {
        this.found = found;
        this.x = x;
        this.y = y;
        log.debug("PlacementResult created: found={}, x={}, y={}", found, x, y);
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