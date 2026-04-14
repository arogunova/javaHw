package ru.hofftech.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Модель кузова грузовика.
 * Размер кузова — 6×6 клеток.
 * Обеспечивает проверку возможности размещения посылки, её размещение и поиск места.
 */
public class Truck {
    public static final int SIZE = 6;

    private final char[][] grid;
    private final List<Object[]> packages;

    /**
     * Конструктор. Создаёт пустой кузов, все клетки заполнены пробелами.
     */
    public Truck() {
        this.grid = new char[SIZE][SIZE];

        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                this.grid[y][x] = ' ';
            }
        }

        this.packages = new ArrayList<>();
    }

    /**
     * Проверяет, можно ли разместить посылку в указанной позиции.
     * Учитывает границы кузова, занятость клеток и правило опоры (>50% основания).
     *
     * @param parcel посылка
     * @param x координата X левого нижнего угла
     * @param y координата Y левого нижнего угла (0 — дно)
     * @return true, если разместить можно
     */
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

    /**
     * Размещает посылку в кузове по результату поиска места.
     *
     * @param parcel посылка
     * @param result результат поиска (содержит координаты)
     * @throws IllegalArgumentException если место не найдено
     */
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

    /**
     * Простой алгоритм поиска места для посылки.
     * Ищет первую подходящую позицию снизу вверх, слева направо.
     *
     * @param parcel посылка
     * @return результат поиска с координатами или флагом неудачи
     */
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

    /**
     * Размещает посылку по заданным координатам.
     *
     * @param parcel посылка
     * @param x координата X
     * @param y координата Y
     */
    public void placePackageAt(Parcel parcel, int x, int y) {
        PlacementResult result = new PlacementResult(true, x, y);
        placePackage(parcel, result);
    }

    /**
     * Возвращает строковое представление кузова в формате:
     * +------+
     * |      |
     * | 99   |
     * +------+
     *
     * @return строковое представление кузова
     */
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

    /**
     * Возвращает список всех размещённых посылок с их координатами.
     * Каждый элемент — массив [Parcel, x, y].
     *
     * @return копия списка размещённых посылок
     */
    public List<Object[]> getPackagesInfo() {
        return new ArrayList<>(packages);
    }
}