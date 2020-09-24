package com.uapp_llc.test.stub.repository.identification;

@FunctionalInterface
public interface Identification<T> {

  void apply(T entity);

}
