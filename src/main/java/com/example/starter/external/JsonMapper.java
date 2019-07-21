package com.example.starter.external;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapper {

  private static JsonMapper ourInstance = new JsonMapper();

  private final ObjectMapper objectMapper;

  public static JsonMapper getInstance() {
    return ourInstance;
  }

  private JsonMapper() {
    this.objectMapper = new ObjectMapper();
  }

  public ObjectMapper getObjectMapper() {
    return objectMapper;
  }
}
