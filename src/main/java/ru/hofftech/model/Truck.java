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
            System.out.println("  Cannot place: too wide (needs " + parcel.getWidth() +
                    ", available " + (SIZE - x) + ")");
            return false;
        }

        if (y + parcel.getHeight() > SIZE) {
            System.out.println("  Cannot place: too tall (needs " + parcel.getHeight() +
                    ", available " + (SIZE - y) + ")");
            return false;
        }

        for (int py = 0; py < parcel.getHeight(); py++) {
            for (int px = 0; px < parcel.getWidth(); px++) {
                List<String> shape = parcel.getShape();
                int shapeRow = parcel.getHeight() - 1 - py;
                String currentLine = shape.get(shapeRow);

                if (px >= currentLine.length()) {
                    continue;
                }

                if (currentLine.charAt(px) == parcel.getSymbol()) {
                    if (grid[y + py][x + px] != ' ') {
                        System.out.println("  Cannot place: cell (" + (x + px) + "," + (y + py) +
                                ") occupied by '" + grid[y + py][x + px] + "'");
                        return false;
                    }
                }
            }
        }

        if (y == 0) {
            System.out.println("  Placing on floor (good support)");
            return true;
        }

        int bottomWidth = 0;
        int supportCount = 0;

        for (int px = 0; px < parcel.getWidth(); px++) {
            List<String> shape = parcel.getShape();
            String bottomLine = shape.get(parcel.getHeight() - 1);

            if (px >= bottomLine.length()) {
                continue;
            }

            if (bottomLine.charAt(px) == parcel.getSymbol()) {
                bottomWidth++;

                if (grid[y - 1][x + px] != ' ') {
                    supportCount++;
                }
            }
        }

        boolean hasGoodSupport = supportCount > bottomWidth / 2;

        System.out.println("  Support: " + supportCount + "/" + bottomWidth +
                " -> " + (hasGoodSupport ? "OK" : "NOT ENOUGH"));

        return hasGoodSupport;
    }

    public void placePackage(Parcel parcel, PlacementResult result) {
        if (!result.isFound()) {
            throw new IllegalArgumentException("Cannot place parcel: no position found");
        }

        int x = result.getX();
        int y = result.getY();

        System.out.println("Placing parcel '" + parcel.getSymbol() +
                "' at (" + x + "," + y + ")");

        for (int py = 0; py < parcel.getHeight(); py++) {
            for (int px = 0; px < parcel.getWidth(); px++) {
                List<String> shape = parcel.getShape();
                int shapeRow = parcel.getHeight() - 1 - py;

                String currentLine = shape.get(shapeRow);
                if (px >= currentLine.length()) {
                    continue;
                }

                if (currentLine.charAt(px) == parcel.getSymbol()) {
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
        System.out.println("  Looking for place for parcel '" + parcel.getSymbol() +
                "' (" + parcel.getWidth() + "x" + parcel.getHeight() + ")");

        for (int y = SIZE - parcel.getHeight(); y >= 0; y--) {
            for (int x = 0; x <= SIZE - parcel.getWidth(); x++) {
                if (canPlace(parcel, x, y)) {
                    System.out.println("    Found place at (" + x + "," + y + ")");
                    return new PlacementResult(true, x, y);
                }
            }
        }

        System.out.println("    No place found in this truck");
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

        for (int y = SIZE - 1; y >= 0; y--) {
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