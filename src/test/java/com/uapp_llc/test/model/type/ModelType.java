package com.uapp_llc.test.model.type;

public interface ModelType<T> {

  String name();

  Class<T> modelClass();

}
