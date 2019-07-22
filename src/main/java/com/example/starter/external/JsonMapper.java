package com.example.starter.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

class JsonMapper {

  private static JsonMapper ourInstance = new JsonMapper();

  private final ObjectMapper objectMapper;

  static JsonMapper getInstance() {
    return ourInstance;
  }

  private JsonMapper() {
    this.objectMapper = new ObjectMapper();
  }

  ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  static JsonNode getSource(JsonNode node) {
    return node.get("_source");
  }
}
