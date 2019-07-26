package com.example.starter.external;

import java.io.IOException;

public interface ElasticGateway<T, S> {

  T getById(String id) throws IOException;

  S save(S t) throws IOException;

  S deleteById(S s) throws  IOException;

  S update(S s, String id) throws IOException;

  S search(S s) throws IOException;

}
