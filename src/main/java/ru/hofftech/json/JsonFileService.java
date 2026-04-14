package ru.hofftech.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hofftech.model.Truck;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Сервис для работы с JSON файлами.
 * Обеспечивает сохранение и загрузку списка машин в/из JSON.
 */
public class JsonFileService {
    private static final Logger log = LoggerFactory.getLogger(JsonFileService.class);
    private static final ObjectMapper mapper = JsonMapper.getMapper();

    /**
     * Сохраняет список машин в JSON файл.
     *
     * @param trucks список машин для сохранения
     * @param filePath путь к выходному JSON файлу
     * @throws IOException если произошла ошибка при записи в файл
     */
    public static void saveToFile(List<Truck> trucks, String filePath) throws IOException {
        log.info("Saving {} trucks to {}", trucks.size(), filePath);

        LoadingResultJson result = JsonConverter.toJson(trucks);
        mapper.writeValue(new File(filePath), result);

        log.info("Successfully saved to {}", filePath);
    }

    /**
     * Загружает список машин из JSON файла.
     *
     * @param filePath путь к JSON файлу
     * @return список загруженных машин
     * @throws IOException если файл не найден или произошла ошибка чтения
     */
    public static List<Truck> loadFromFile(String filePath) throws IOException {
        log.info("Loading trucks from {}", filePath);

        File file = new File(filePath);
        if (!file.exists()) {
            log.error("File not found: {}", filePath);
            throw new IOException("File not found: " + filePath);
        }

        LoadingResultJson result = mapper.readValue(file, LoadingResultJson.class);
        List<Truck> trucks = JsonConverter.fromJson(result);

        log.info("Successfully loaded {} trucks", trucks.size());
        return trucks;
    }
}