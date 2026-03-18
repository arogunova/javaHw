package ru.hofftech.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hofftech.model.Truck;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Сервис для сохранения и загрузки результатов в JSON.
 */
public class JsonFileService {
    private static final Logger log = LoggerFactory.getLogger(JsonFileService.class);
    private static final ObjectMapper mapper = JsonMapper.getMapper();

    /**
     * Сохраняет список машин в JSON файл.
     */
    public static void saveToFile(List<Truck> trucks, String filePath) throws IOException {
        log.info("Saving {} trucks to {}", trucks.size(), filePath);

        // Конвертируем в JSON-модель
        LoadingResultJson result = JsonConverter.toJson(trucks);

        // Записываем в файл
        mapper.writeValue(new File(filePath), result);

        log.info("Successfully saved to {}", filePath);
    }

    /**
     * Загружает список машин из JSON файла.
     */
    public static List<Truck> loadFromFile(String filePath) throws IOException {
        log.info("Loading trucks from {}", filePath);

        // Проверим, что файл существует
        File file = new File(filePath);
        if (!file.exists()) {
            log.error("File not found: {}", filePath);
            throw new IOException("File not found: " + filePath);
        }

        // Читаем из файла
        LoadingResultJson result = mapper.readValue(new File(filePath), LoadingResultJson.class);

        // Конвертируем обратно в Truck
        List<Truck> trucks = JsonConverter.fromJson(result);

        log.info("Successfully loaded {} trucks", trucks.size());
        return trucks;
    }
}