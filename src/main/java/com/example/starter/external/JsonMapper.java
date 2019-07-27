package com.example.starter.external;

import com.fasterxml.jackson.databind.JsonNode;

class JsonMapper {

  static JsonNode getSource(JsonNode node) {
    return node.get("_source");
  }

  static JsonNode getHits(JsonNode node) {
    return node.get("hits");
  }
}
