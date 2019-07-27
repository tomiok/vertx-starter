package com.example.starter.external;

import com.example.starter.model.Movie;
import com.fasterxml.jackson.databind.JsonNode;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import java.io.IOException;
import java.io.InputStream;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

public class ElasticGatewayImpl implements ElasticGateway<Movie, String> {

  @Override
  public Movie getById(final String id) throws IOException {
    RestClient rc = HttpClient.getInstance().getRestClient();

    Request request = new Request(HttpMethod.GET.name(), "/movies/_doc/" + id);
    JsonNode source = JsonMapper
        .getSource(
            Json.mapper.readTree(rc.performRequest(request).getEntity().getContent()));

    return Json.mapper.treeToValue(source, Movie.class);
  }

  /**
   * This implementation is not RESTful due to elasticsearch API will not return the movie created.
   *
   * @param movie The movie to be indexed
   *
   * @return The document
   *
   * @throws IOException - in case something is wrong with the http call.
   */
  @Override
  public String save(final String movie) throws IOException {
    RestClient rc = HttpClient.getInstance().getRestClient();
    Request request = new Request(HttpMethod.POST.name(), "/movies/_doc");
    request.setJsonEntity(movie);

    Response res = rc.performRequest(request);

    JsonNode node = Json
        .mapper
        .readTree(res.getEntity().getContent());

    return node.toString();
  }

  @Override
  public String deleteById(final String id) throws IOException {
    RestClient rc = HttpClient.getInstance().getRestClient();
    Request request = new Request(HttpMethod.DELETE.name(), "/movies/_doc/" + id);
    Response res = rc.performRequest(request);
    JsonNode node = Json
        .mapper
        .readTree(res.getEntity().getContent());

    return node.toString();
  }

  @Override
  public String update(final String s, final String id) throws IOException {
    RestClient rc = HttpClient.getInstance().getRestClient();
    Request request = new Request(HttpMethod.PUT.name(), "/movies/_doc/" + id);
    request.setJsonEntity(s);

    InputStream content = rc.performRequest(request).getEntity().getContent();

    return Json
        .mapper
        .readTree(content)
        .toString();
  }

  @Override
  public String search(final String s) throws IOException {
    RestClient rc = HttpClient.getInstance().getRestClient();
    Request request = new Request(HttpMethod.GET.name(), "/movies/_search/");
    request.setJsonEntity(s);

    JsonNode source = JsonMapper
        .getHits(
            Json.mapper.readTree(rc.performRequest(request).getEntity().getContent()));

    return source.toString();
  }
}
