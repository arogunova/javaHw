package ru.hofftech.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Утилитный класс для работы с Jackson ObjectMapper.
 * Создаёт и предоставляет единственный экземпляр маппера для всего приложения.
 * ObjectMapper потокобезопасен и его создание — дорогая операция,
 * поэтому он создаётся один раз и переиспользуется.
 */
public class JsonMapper {
    private static final Logger log = LoggerFactory.getLogger(JsonMapper.class);

    /** Единственный экземпляр маппера на всё приложение */
    private static final ObjectMapper MAPPER = createMapper();

    /**
     * Приватный конструктор запрещает создание экземпляров класса.
     */
    private JsonMapper() {
        log.debug("JsonMapper constructor called (should be only once)");
    }

    /**
     * Создаёт и настраивает ObjectMapper.
     * Включает красивое форматирование JSON (INDENT_OUTPUT).
     *
     * @return настроенный экземпляр ObjectMapper
     */
    private static ObjectMapper createMapper() {
        log.debug("Creating ObjectMapper (once)");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }

    /**
     * Возвращает единственный экземпляр ObjectMapper.
     *
     * @return ObjectMapper для сериализации/десериализации JSON
     */
    public static ObjectMapper getMapper() {
        return MAPPER;
    }
}