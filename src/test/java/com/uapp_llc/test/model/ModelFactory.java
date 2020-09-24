package com.uapp_llc.test.model;

public abstract class ModelFactory<T> {

  public abstract T createModel(ModelType<T> type);

}
