package com.example.starter.external;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapper {

  private static JsonMapper ourInstance = new JsonMapper();

  private final ObjectMapper objectMapper;

  public static JsonMapper getInstance() {
    return ourInstance;
  }

  private JsonMapper() {
    this.objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public ObjectMapper getObjectMapper() {
    return objectMapper;
  }
}
