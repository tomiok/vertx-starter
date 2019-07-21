package com.example.starter;

import com.example.starter.external.HttpClient;
import com.example.starter.model.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;

public class MainVerticle extends AbstractVerticle {

  private static final int PORT = 8888;

  private List<Movie> holder = new ArrayList<>();

  @Override
  public void start(Future<Void> startFuture) {

    final Router router = Router.router(vertx);
    router.get("/api/movies").handler(this::getAll);
    router.get("/api/movies/:title").handler(this::getOne);

    router.route("/api/movies/*").handler(BodyHandler.create());
    router.post("/api/movies").handler(this::add);

    router.get("/api/latest/movies").handler(this::getLatest);

    vertx.createHttpServer().requestHandler(router)
      .listen(PORT, httpResponse -> {
        if (httpResponse.succeeded()) {
          startFuture.complete();
        } else {
          System.out.println(httpResponse);
          startFuture.fail(httpResponse.cause());
        }
      });
  }

  private List<Movie> getAllMovies() {
    return Arrays.asList(new Movie("Tha hackerman", "Elliot", 1999, "Amazing hacker movie"),
      new Movie("Terminator", "Arnold", 2001, "The T1000 is coming from the future"),
      new Movie("Jackass", "Jhonny Noxville", 2010, "Stupid movie"));
  }

  private void getAll(RoutingContext rc) {
    rc.response()
      .setStatusCode(200)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(getAllMovies()));
  }

  private Movie getOne(String title) {
    return new Movie(title, "NN", 2009, "IDK bro");
  }

  private void getOne(RoutingContext rc) {
    String title = rc.request().getParam("title");
    rc
      .response()
      .setStatusCode(200)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(getOne(title)));
  }

  private void add(RoutingContext rc) {
    JsonObject body = rc.getBodyAsJson();
    ObjectMapper mapper = new ObjectMapper();

    RestClient client = HttpClient.getInstance().getRestClient();

    Request request = new Request("POST", "/movies/_doc");
    request.setJsonEntity(body.encode());
    try {
      client.performRequest(request);
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      Movie movie = mapper.readValue(body.toString(), Movie.class);
      holder.add(movie);
    } catch (IOException e) {
      e.printStackTrace();
    }
    rc.response()
      .setStatusCode(201)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(body));
  }

  private void getLatest(RoutingContext rc) {
    rc.response().setStatusCode(200)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(this.holder));
  }
}
