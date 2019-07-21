package com.example.starter.external;

import com.example.starter.model.Movie;
import io.vertx.core.http.HttpMethod;
import java.io.IOException;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

public class ElasticGatewayImpl implements ElasticGateway<Movie, String> {

  @Override
  public Movie findById(final String id) throws IOException {
    RestClient rc = HttpClient.getInstance().getRestClient();

    Request request = new Request(HttpMethod.GET.name(), "/movies/_doc/" + id);
    return JsonMapper
        .getInstance()
        .getObjectMapper()
        .readValue(rc.performRequest(request).getEntity().getContent(), Movie.class);
  }

  @Override
  public Movie save(final String movie) throws IOException {
    RestClient rc = HttpClient.getInstance().getRestClient();
    Request request = new Request(HttpMethod.POST.name(), "/movies/_doc");
    request.setJsonEntity(movie);

    Response res =
        rc.performRequest(request);

    return JsonMapper
        .getInstance()
        .getObjectMapper()
        .readValue(res.getEntity().getContent(), Movie.class);
  }
}
