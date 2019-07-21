package com.example.starter.external;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

public class HttpClient {

  private static HttpClient httpClient = null;

  private final RestClient restClient;

  private HttpClient() {
    restClient = RestClient.builder(
        new HttpHost("localhost", 9200, "http"),
        new HttpHost("localhost", 9201, "http")).build();
  }

  public static HttpClient getInstance() {
    if (httpClient == null) {
      return new HttpClient();
    }
    return httpClient;
  }

  public RestClient getRestClient() {
    return this.restClient;
  }
}
