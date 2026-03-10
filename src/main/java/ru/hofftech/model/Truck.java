package ru.hofftech.model;

// Импорты для работы с коллекциями
import java.util.ArrayList;
import java.util.List;

/**
 * Класс Truck (Кузов машины) - модель данных, представляющая кузов грузовика.
 *
 * Размер кузова: 6x6 клеток (константа SIZE)
 *
 * Что умеет:
 * - Хранить сетку 6x6 с размещенными посылками
 * - Проверять, можно ли разместить посылку в определенной позиции
 * - Размещать посылку
 * - Проверять опору (больше половины основания должно на чем-то стоять)
 * - Выводить кузов в нужном формате
 */
public class Truck {
    // =============== КОНСТАНТЫ ===============

    /** Размер кузова (6x6) - константа для всего класса */
    public static final int SIZE = 6;

    // =============== ПОЛЯ КЛАССА ===============

    /**
     * grid - двумерный массив, представляющий кузов.
     * Размер: SIZE x SIZE (6x6)
     * Индексация: grid[строка][столбец] или grid[y][x]
     * Каждая ячейка содержит:
     * - символ посылки (например '9'), если там есть посылка
     * - пробел ' ', если место свободно
     */
    private final char[][] grid;

    /**
     * packages - список размещенных посылок.
     * Храним не только сами посылки, но и их координаты.
     * Каждый элемент - это массив Object, где:
     * - [0] - посылка (Parcel)
     * - [1] - координата X (Integer)
     * - [2] - координата Y (Integer)
     *
     * Используем List<Object[]> вместо отдельных классов для простоты,
     * так как этот список используется только внутри Truck.
     */
    private final List<Object[]> packages;

    // =============== КОНСТРУКТОР ===============

    /**
     * Конструктор - создает пустой кузов.
     * Заполняет всю сетку пробелами.
     */
    public Truck() {
        // Шаг 1: создаем двумерный массив размером SIZE x SIZE
        this.grid = new char[SIZE][SIZE];

        // Шаг 2: заполняем все ячейки пробелами
        // Внешний цикл - по строкам (Y координата)
        for (int y = 0; y < SIZE; y++) {
            // Внутренний цикл - по столбцам (X координата)
            for (int x = 0; x < SIZE; x++) {
                this.grid[y][x] = ' '; // пробел означает пустое место
            }
        }

        // Шаг 3: создаем пустой список для хранения информации о посылках
        this.packages = new ArrayList<>();

        // Можно добавить отладочный вывод
        // System.out.println("New empty truck created");
    }

    // =============== МЕТОДЫ ДЛЯ РАБОТЫ С ПОСЫЛКАМИ ===============

    /**
     * Проверяет, можно ли разместить посылку в указанной позиции.
     * Учитывает три правила:
     * 1. Посылка не должна выходить за границы кузова
     * 2. Место должно быть свободно (нет других посылок)
     * 3. Опора должна быть больше половины основания
     *
     * @param parcel посылка, которую хотим разместить
     * @param x координата X верхнего левого угла (0-5)
     * @param y координата Y верхнего левого угла (0-5)
     * @return true если разместить можно, false если нельзя
     */
    public boolean canPlace(Parcel parcel, int x, int y) {
        // ===== ПРОВЕРКА 1: Границы кузова =====

        // Проверяем, не выходит ли посылка за правый край
        // x + ширина посылки должно быть <= SIZE (6)
        if (x + parcel.getWidth() > SIZE) {
            System.out.println("  Cannot place: too wide (needs " + parcel.getWidth() +
                    ", available " + (SIZE - x) + ")"); // отладка
            return false;
        }

        // Проверяем, не выходит ли посылка за нижний край
        // y + высота посылки должно быть <= SIZE (6)
        if (y + parcel.getHeight() > SIZE) {
            System.out.println("  Cannot place: too tall (needs " + parcel.getHeight() +
                    ", available " + (SIZE - y) + ")"); // отладка
            return false;
        }

        // ===== ПРОВЕРКА 2: Свободное место =====
        // Проходим по всем клеткам, которые займет посылка
        for (int py = 0; py < parcel.getHeight(); py++) {
            for (int px = 0; px < parcel.getWidth(); px++) {
                // Получаем форму посылки (список строк)
                List<String> shape = parcel.getShape();

                // Проверяем, есть ли в этом месте часть посылки
                // shape.get(py) - строка посылки
                // .charAt(px) - символ в этой строке
                // Сравниваем с символом посылки (например '9')
                if (shape.get(py).charAt(px) == parcel.getSymbol()) {
                    // Проверяем, свободно ли это место в кузове
                    // grid[строка][столбец] - ячейка кузова
                    if (grid[y + py][x + px] != ' ') {
                        System.out.println("  Cannot place: cell (" + (x + px) + "," +
                                (y + py) + ") occupied by '" + grid[y + py][x + px] + "'"); // отладка
                        return false;
                    }
                }
                // Если в форме посылки в этом месте другой символ (например пробел),
                // то пропускаем - это пустота внутри посылки (как в букве О)
            }
        }

        // ===== ПРОВЕРКА 3: Опора (самое сложное правило) =====
        // Правило из задания: "больше половины основания должно на чем-то стоять"

        // Находим нижнюю строку посылки (Y координата + высота - 1)
        // Например, если посылка высотой 3 и стоит на y=2, то низ будет на y=4
        int bottomY = y + parcel.getHeight() - 1;

        // Особый случай: если посылка стоит на дне кузова (нижняя строка кузова)
        // Дно всегда обеспечивает 100% опору
        if (bottomY == SIZE - 1) {
            System.out.println("  Placing on floor (good support)"); // отладка
            return true; // на дне всегда есть опора
        }

        // Считаем опору:
        // bottomWidth - сколько клеток в основании посылки
        // supportCount - сколько из этих клеток имеют опору (клетку снизу)
        int bottomWidth = 0;      // ширина основания (количество клеток в нижнем ряду)
        int supportCount = 0;     // количество клеток с опорой

        // Проходим по всем клеткам нижней строки посылки
        for (int px = 0; px < parcel.getWidth(); px++) {
            // Получаем форму посылки
            List<String> shape = parcel.getShape();

            // Проверяем, есть ли в этом месте часть посылки
            // parcel.getHeight() - 1 - это последняя строка (основание)
            if (shape.get(parcel.getHeight() - 1).charAt(px) == parcel.getSymbol()) {
                bottomWidth++; // нашли клетку основания - увеличиваем счетчик

                // Проверяем, есть ли опора снизу
                // bottomY + 1 - клетка прямо под основанием
                if (grid[bottomY + 1][x + px] != ' ') {
                    supportCount++; // есть опора (стоит на другой посылке)
                }
            }
        }

        // Проверяем правило: опора должна быть БОЛЬШЕ половины основания
        // supportCount > bottomWidth / 2
        // Например:
        // - ширина 3: нужно > 1.5, т.е. минимум 2
        // - ширина 2: нужно > 1, т.е. минимум 2 (обе клетки должны стоять)
        // - ширина 1: нужно > 0.5, т.е. минимум 1
        boolean hasGoodSupport = supportCount > bottomWidth / 2;

        System.out.println("  Support: " + supportCount + "/" + bottomWidth +
                " -> " + (hasGoodSupport ? "OK" : "NOT ENOUGH")); // отладка

        return hasGoodSupport;
    }

    /**
     * Размещает посылку в кузове в позиции из PlacementResult.
     *
     * @param parcel посылка для размещения
     * @param result результат поиска места (содержит координаты)
     */
    public void placePackage(Parcel parcel, PlacementResult result) {
        // Проверяем, что результат действительно содержит место
        if (!result.isFound()) {
            throw new IllegalArgumentException("Cannot place parcel: no position found");
        }

        // Получаем координаты из результата
        int x = result.getX();
        int y = result.getY();

        System.out.println("Placing parcel '" + parcel.getSymbol() +
                "' at (" + x + "," + y + ")"); // отладка

        // Проходим по всем клеткам посылки
        for (int py = 0; py < parcel.getHeight(); py++) {
            for (int px = 0; px < parcel.getWidth(); px++) {
                // Получаем форму посылки
                List<String> shape = parcel.getShape();

                // Если в этом месте есть часть посылки
                if (shape.get(py).charAt(px) == parcel.getSymbol()) {
                    // Записываем символ в сетку кузова
                    grid[y + py][x + px] = parcel.getSymbol();
                }
                // Если в форме посылки пробел - ничего не делаем
            }
        }

        // Сохраняем информацию о размещенной посылке
        // Используем массив Object[], потому что храним разные типы
        Object[] packageInfo = new Object[3];
        packageInfo[0] = parcel;      // сама посылка
        packageInfo[1] = x;           // координата X (Integer - автоупаковка)
        packageInfo[2] = y;           // координата Y (Integer - автоупаковка)

        packages.add(packageInfo);

        // Можно распечатать текущее состояние кузова для отладки
        // System.out.println(this); // раскомментируй, чтобы видеть процесс
    }

    /**
     * Простой алгоритм поиска места: ищем первую свободную позицию.
     * Проходим сверху вниз, слева направо.
     *
     * @param parcel посылка для размещения
     * @return PlacementResult с результатом поиска
     */
    public PlacementResult findPositionSimple(Parcel parcel) {
        System.out.println("  Looking for place for parcel '" + parcel.getSymbol() +
                "' (" + parcel.getWidth() + "x" + parcel.getHeight() + ")"); // отладка

        // Проходим по всем возможным позициям
        // y от 0 до SIZE - height (чтобы посылка не вылезла за край)
        for (int y = 0; y <= SIZE - parcel.getHeight(); y++) {
            // x от 0 до SIZE - width
            for (int x = 0; x <= SIZE - parcel.getWidth(); x++) {
                // Проверяем, можно ли разместить
                if (canPlace(parcel, x, y)) {
                    System.out.println("    Found place at (" + x + "," + y + ")"); // отладка
                    return new PlacementResult(true, x, y);
                }
            }
        }

        System.out.println("    No place found in this truck"); // отладка
        return new PlacementResult(false);
    }

    // =============== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ===============

    /**
     * Проверяет, пуст ли кузов.
     *
     * @return true если в кузове нет посылок
     */
    public boolean isEmpty() {
        return packages.isEmpty();
    }

    /**
     * Возвращает количество размещенных посылок.
     *
     * @return количество посылок в кузове
     */
    public int getPackagesCount() {
        return packages.size();
    }

    /**
     * Возвращает копию сетки кузова.
     * Возвращаем копию, чтобы внешний код не мог изменить оригинал!
     *
     * @return двумерный массив - копия grid
     */
    public char[][] getGrid() {
        // Создаем новый массив такого же размера
        char[][] copy = new char[SIZE][SIZE];

        // Копируем каждую строку
        for (int i = 0; i < SIZE; i++) {
            // System.arraycopy - быстрый способ скопировать массив
            // источник, позиция в источнике, приемник, позиция в приемнике, длина
            System.arraycopy(grid[i], 0, copy[i], 0, SIZE);
        }

        return copy;
    }

    // =============== МЕТОДЫ ДЛЯ ВЫВОДА ===============

    /**
     * Преобразует кузов в строку для вывода в формате задания.
     * Формат:
     * +------+
     * |      |
     * | 99   |
     * | 99   |
     * +------+
     *
     * @return строковое представление кузова
     */
    @Override
    public String toString() {
        // Используем StringBuilder для эффективного создания строки
        // (он быстрее, чем обычная конкатенация строк)
        StringBuilder result = new StringBuilder();

        // ===== ВЕРХНЯЯ ГРАНИЦА =====
        // Начинаем с '+'
        result.append('+');

        // Добавляем SIZE символов '+' (верхняя граница)
        for (int i = 0; i < SIZE; i++) {
            result.append('+');
        }

        // Заканчиваем '+'
        result.append('+');
        result.append('\n'); // перевод строки

        // ===== СОДЕРЖИМОЕ =====
        // Проходим по всем строкам кузова (Y координата)
        for (int y = 0; y < SIZE; y++) {
            // Левая граница
            result.append('+');

            // Содержимое строки - все клетки в этой строке
            for (int x = 0; x < SIZE; x++) {
                result.append(grid[y][x]);
            }

            // Правая граница
            result.append('+');
            result.append('\n');
        }

        // ===== НИЖНЯЯ ГРАНИЦА =====
        // Аналогично верхней
        result.append('+');
        for (int i = 0; i < SIZE; i++) {
            result.append('+');
        }
        result.append('+');

        return result.toString();
    }

    /**
     * Дополнительный метод для отладки - выводит сетку с координатами.
     * Показывает, какие клетки заняты и какими символами.
     */
    public void printDebug() {
        System.out.println("Truck grid (6x6):");
        System.out.println("   0  1  2  3  4  5");
        for (int y = 0; y < SIZE; y++) {
            System.out.print(y + " ");
            for (int x = 0; x < SIZE; x++) {
                System.out.print("[" + grid[y][x] + "]");
            }
            System.out.println();
        }
    }
}