package com.example.starter.external;

import java.io.IOException;

public interface ElasticGateway<T, S> {

  T findById(String id) throws IOException;

  T save(S t) throws IOException;

}
