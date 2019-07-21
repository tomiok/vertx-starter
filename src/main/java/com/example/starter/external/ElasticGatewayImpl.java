package com.example.starter.external;

import com.example.starter.model.Movie;
import io.vertx.core.http.HttpMethod;
import java.io.IOException;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

public class ElasticGatewayImpl implements ElasticGateway<Movie, String> {

  @Override
  public Movie getById(final String id) throws IOException {
    RestClient rc = HttpClient.getInstance().getRestClient();

    Request request = new Request(HttpMethod.GET.name(), "/movies/_doc/" + id);
    return JsonMapper
        .getInstance()
        .getObjectMapper()
        .readValue(rc.performRequest(request).getEntity().getContent(), Movie.class);
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

    Response res =
        rc.performRequest(request);

    return JsonMapper
        .getInstance()
        .getObjectMapper()
        .readTree(res.getEntity().getContent()).asText();
  }
}
