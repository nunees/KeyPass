package com.keypass.server.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertToJson(Object object) throws JsonProcessingException{
        return objectMapper.writeValueAsString(object);
    }

    public static <T> T convertFromJson(String json, Class<T> objectType) throws JsonProcessingException{
        return objectMapper.readValue(json, objectType);
    }
}
