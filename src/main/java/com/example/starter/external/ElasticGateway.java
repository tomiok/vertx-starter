package com.example.starter.external;

import java.io.IOException;

public interface ElasticGateway<T, S> {

  T getById(String id) throws IOException;

  S save(S t) throws IOException;

}
