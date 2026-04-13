package ru.hofftech.file;

import ru.hofftech.model.Parcel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс для чтения посылок из текстового файла.
 * Формат файла: посылки разделены пустыми строками, каждая посылка представлена несколькими строками текста.
 */
public class ParcelFileReader {
    private static final Logger log = LoggerFactory.getLogger(ParcelFileReader.class);

    /**
     * Читает файл и возвращает список посылок.
     *
     * @param filePath путь к файлу с посылками
     * @return список посылок
     * @throws IOException если файл не найден или не читается
     */
    public List<Parcel> readFromFile(String filePath) throws IOException {
        log.info("Reading file: {}", filePath);

        List<String> allLines = Files.readAllLines(Paths.get(filePath));
        List<Parcel> parcels = new ArrayList<>();
        List<String> currentParcelLines = new ArrayList<>();

        for (String line : allLines) {
            if (line.trim().isEmpty()) {
                if (!currentParcelLines.isEmpty()) {
                    Parcel parcel = createParcel(currentParcelLines);
                    parcels.add(parcel);
                    currentParcelLines = new ArrayList<>();
                    log.debug("Found parcel with symbol: {}", parcel.getSymbol());
                }
            } else {
                currentParcelLines.add(line);
            }
        }

        if (!currentParcelLines.isEmpty()) {
            Parcel parcel = createParcel(currentParcelLines);
            parcels.add(parcel);
            log.debug("Found last parcel with symbol: {}", parcel.getSymbol());
        }

        log.info("Total parcels found: {}", parcels.size());
        return parcels;
    }

    /**
     * Создаёт объект Parcel из списка строк.
     * Определяет символ посылки как первый непробельный символ.
     *
     * @param lines строки формы посылки
     * @return созданная посылка
     * @throws IllegalArgumentException если в форме нет видимых символов
     */
    private Parcel createParcel(List<String> lines) {
        char symbol = '?';
        for (String line : lines) {
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (!Character.isWhitespace(c)) {
                    symbol = c;
                    break;
                }
            }
            if (symbol != '?') break;
        }

        if (symbol == '?') {
            log.error("Parcel has no visible symbols in lines: {}", lines);
            throw new IllegalArgumentException("Parcel has no visible symbols");
        }

        log.debug("Created parcel with symbol: {}", symbol);
        String name = "Посылка из файла";
        return new Parcel(name, lines, symbol);
    }

    /**
     * Выводит информацию о всех посылках на экран.
     *
     * @param parcels список посылок для печати
     */
    public void printParcels(List<Parcel> parcels) {
        System.out.println("Total parcels: " + parcels.size());
        System.out.println("-------------------");

        for (int i = 0; i < parcels.size(); i++) {
            Parcel parcel = parcels.get(i);

            System.out.println("\nParcel #" + (i + 1));
            System.out.println("Info: " + parcel);
            System.out.println("Shape:");

            for (String line : parcel.getShape()) {
                System.out.println("  " + line);
            }
        }
    }
}