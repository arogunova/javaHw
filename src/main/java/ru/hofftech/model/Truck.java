package ru.hofftech.model;

import java.util.ArrayList;
import java.util.List;

public class Truck {
    public static final int SIZE = 6;

    private final char[][] grid;
    private final List<Object[]> packages;

    public Truck() {
        this.grid = new char[SIZE][SIZE];
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                this.grid[y][x] = ' ';
            }
        }
        this.packages = new ArrayList<>();
    }

    public boolean canPlace(Parcel parcel, int x, int y) {
        if (x + parcel.getWidth() > SIZE) {
            return false;
        }
        if (y + parcel.getHeight() > SIZE) {
            return false;
        }

        for (int py = 0; py < parcel.getHeight(); py++) {
            for (int px = 0; px < parcel.getWidth(); px++) {
                List<String> shape = parcel.getShape();
                if (shape.get(py).charAt(px) == parcel.getSymbol()) {
                    if (grid[y + py][x + px] != ' ') {
                        return false;
                    }
                }
            }
        }

        int bottomY = y + parcel.getHeight() - 1;
        if (bottomY == SIZE - 1) {
            return true;
        }

        int bottomWidth = 0;
        int supportCount = 0;

        for (int px = 0; px < parcel.getWidth(); px++) {
            List<String> shape = parcel.getShape();
            if (shape.get(parcel.getHeight() - 1).charAt(px) == parcel.getSymbol()) {
                bottomWidth++;
                if (grid[bottomY + 1][x + px] != ' ') {
                    supportCount++;
                }
            }
        }

        return supportCount > bottomWidth / 2;
    }

    public void placePackage(Parcel parcel, PlacementResult result) {
        if (!result.isFound()) {
            throw new IllegalArgumentException("Cannot place parcel: no position found");
        }

        int x = result.getX();
        int y = result.getY();

        for (int py = 0; py < parcel.getHeight(); py++) {
            for (int px = 0; px < parcel.getWidth(); px++) {
                List<String> shape = parcel.getShape();
                if (shape.get(py).charAt(px) == parcel.getSymbol()) {
                    grid[y + py][x + px] = parcel.getSymbol();
                }
            }
        }

        Object[] packageInfo = new Object[3];
        packageInfo[0] = parcel;
        packageInfo[1] = x;
        packageInfo[2] = y;
        packages.add(packageInfo);
    }

    public PlacementResult findPositionSimple(Parcel parcel) {
        for (int y = 0; y <= SIZE - parcel.getHeight(); y++) {
            for (int x = 0; x <= SIZE - parcel.getWidth(); x++) {
                if (canPlace(parcel, x, y)) {
                    return new PlacementResult(true, x, y);
                }
            }
        }
        return new PlacementResult(false);
    }

    public boolean isEmpty() {
        return packages.isEmpty();
    }

    public int getPackagesCount() {
        return packages.size();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append('+');
        result.append("+".repeat(SIZE));
        result.append('+');
        result.append('\n');

        for (int y = 0; y < SIZE; y++) {
            result.append('+');
            for (int x = 0; x < SIZE; x++) {
                result.append(grid[y][x]);
            }
            result.append('+');
            result.append('\n');
        }

        result.append('+');
        result.append("+".repeat(SIZE));
        result.append('+');
        return result.toString();
    }
}