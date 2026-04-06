package ru.hofftech.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonMapper {
    private static final Logger log = LoggerFactory.getLogger(JsonMapper.class);

    private static final ObjectMapper MAPPER = createMapper();

    private JsonMapper() {
        log.debug("JsonMapper constructor called (should be only once)");
    }

    private static ObjectMapper createMapper() {
        log.debug("Creating ObjectMapper (once)");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }

    public static ObjectMapper getMapper() {
        return MAPPER;
    }
}