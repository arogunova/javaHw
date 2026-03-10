package ru.hofftech.file;

// Импортируем класс Parcel из пакета model
// Обрати внимание: нужно указывать полный путь, потому что Parcel в другом пакете
import ru.hofftech.model.Parcel;

// Импорты для работы с файлами и коллекциями
import java.io.IOException;                    // для обработки ошибок ввода-вывода
import java.nio.file.Files;                    // утилитный класс для работы с файлами
import java.nio.file.Paths;                     // для создания пути к файлу
import java.util.ArrayList;                      // динамический массив
import java.util.List;                           // интерфейс списка

/**
 * Класс для чтения посылок из файла.

 * Задача класса: прочитать файл со специальным форматом и превратить его
 * в список объектов Parcel (посылок), с которыми удобно работать дальше.

 * Формат файла:
 * - Каждая посылка представлена несколькими строками текста
 * - Посылки разделены пустыми строками
 * - Символы в строках показывают форму посылки (например, '9', '1', '3')
 */
public class ParcelFileReader {

    /**
     * Главный метод класса - читает файл и возвращает список посылок.
     *
     * @param filePath путь к файлу с посылками (например, "C:/test.txt")
     * @return список объектов Parcel (посылок)
     * @throws IOException если файл не найден или не читается
     */
    public List<Parcel> readFromFile(String filePath) throws IOException {
        // Шаг 1: читаем все строки из файла в список
        // Files.readAllLines - удобный метод, который сразу возвращает List<String>
        // Paths.get(filePath) - преобразует строку пути в объект Path
        System.out.println("Reading file: " + filePath); // отладочный вывод

        // Читаем все строки файла
        // Каждый элемент списка - одна строка из файла
        List<String> allLines = Files.readAllLines(Paths.get(filePath));

        // Шаг 2: создаем список, куда будем складывать готовые посылки
        List<Parcel> parcels = new ArrayList<>();

        // Шаг 3: временное хранилище для строк текущей посылки
        // Пока мы читаем файл, мы собираем строки одной посылки сюда
        List<String> currentParcelLines = new ArrayList<>();

        // Шаг 4: проходим по всем строкам файла
        // allLines - это все строки файла, которые мы прочитали
        for (String line : allLines) {
            // line.trim() - убираем пробелы в начале и конце строки
            // .isEmpty() - проверяем, стала ли строка пустой
            if (line.trim().isEmpty()) {
                // Это пустая строка - значит текущая посылка закончилась

                // Проверяем, что мы вообще что-то собрали
                if (!currentParcelLines.isEmpty()) {
                    // Создаем посылку из собранных строк
                    Parcel parcel = createParcel(currentParcelLines);
                    // Добавляем посылку в общий список
                    parcels.add(parcel);

                    // Начинаем новую посылку - создаем новый пустой список
                    currentParcelLines = new ArrayList<>();

                    System.out.println("Found parcel with symbol: " + parcel.getSymbol()); // отладка
                }
            } else {
                // Это не пустая строка - добавляем её к текущей посылке
                currentParcelLines.add(line);
            }
        }

        // Шаг 5: важная проверка!
        // После цикла могла остаться последняя посылка (если файл не заканчивается пустой строкой)
        if (!currentParcelLines.isEmpty()) {
            Parcel parcel = createParcel(currentParcelLines);
            parcels.add(parcel);
            System.out.println("Found last parcel with symbol: " + parcel.getSymbol()); // отладка
        }

        // Шаг 6: возвращаем результат
        System.out.println("Total parcels found: " + parcels.size()); // отладка
        return parcels;
    }

    /**
     * Вспомогательный метод - создает объект Parcel из списка строк.
     *
     * @param lines список строк, из которых состоит одна посылка
     * @return готовый объект Parcel
     */
    private Parcel createParcel(List<String> lines) {
        // Нужно определить, из какого символа состоит посылка
        // Обычно это первый символ первой непустой строки
        char symbol = '?'; // значение по умолчанию, если не найдем символ

        // Проходим по всем строкам посылки
        for (String line : lines) {
            // Ищем первую непустую строку
            if (!line.trim().isEmpty()) {
                // Берем первый символ этой строки
                symbol = line.charAt(0);
                break; // выходим из цикла, так как символ найден
            }
        }

        // Создаем и возвращаем новую посылку
        // Конструктор Parcel сам вычислит ширину, высоту и площадь
        return new Parcel(lines, symbol);
    }

    /**
     * Вспомогательный метод для отладки - печатает информацию о всех посылках.
     *
     * @param parcels список посылок для печати
     */
    public void printParcels(List<Parcel> parcels) {
        System.out.println("Total parcels: " + parcels.size());
        System.out.println("-------------------");

        // Проходим по всем посылкам
        for (int i = 0; i < parcels.size(); i++) {
            Parcel parcel = parcels.get(i); // берем i-ю посылку

            System.out.println("\nParcel #" + (i + 1)); // нумерация с 1 для людей
            System.out.println("Info: " + parcel); // toString() сработает автоматически
            System.out.println("Shape:");

            // Печатаем каждую строку формы посылки
            // Добавляем отступ в 2 пробела для красоты
            for (String line : parcel.getShape()) {
                System.out.println("  " + line);
            }
        }
    }
}