package com.example.starter;

import com.example.starter.external.ElasticGateway;
import com.example.starter.external.ElasticGatewayImpl;
import com.example.starter.model.Movie;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainVerticle extends AbstractVerticle {

  private static final int PORT = 8888;

  private ElasticGateway<Movie, String> gateway;

  public MainVerticle() {
    this.gateway = new ElasticGatewayImpl();
  }

  @Override
  public void start(Future<Void> startFuture) {

    final Router router = Router.router(vertx);
    router.get("/api/movies").handler(this::getAll);
    router.get("/api/movies/:id").handler(this::getById);

    router.route("/api/movies/*").handler(BodyHandler.create());
    router.post("/api/movies").handler(this::add);

    router.get("/api/latest/movies").handler(this::getById);

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

  private void add(RoutingContext rc) {
    JsonObject body = rc.getBodyAsJson();

    String documentSaved = null;
    try {
      documentSaved = gateway.save(body.encode());
    } catch (IOException e) {
      e.printStackTrace();
    }
    rc.response()
        .setStatusCode(201)
        .putHeader("content-type", "application/json; charset=utf-8")
        .end(Json.encodePrettily(documentSaved));
  }

  private void getById(RoutingContext rc) {
    String id = rc.request().getParam("id");
    try {
      rc.response().setStatusCode(200)
          .putHeader("content-type", "application/json; charset=utf-8")
          .end(Json.encodePrettily(gateway.getById(id)));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
