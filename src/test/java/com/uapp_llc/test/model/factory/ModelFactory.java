package com.uapp_llc.test.model.factory;

import com.uapp_llc.test.model.type.ModelType;
import com.uapp_llc.test.model.wrapper.ModelWrapper;

public final class ModelFactory {

  private ModelFactory() {
  }

  public static <T> ModelWrapper<T> createWrapper(ModelType<T> type) {
    Class<T> modelClass = type.modelClass();
    AbstractFactory<T> factory = FactoryProducer.getFactory(modelClass);
    return factory.createWrapper(type);
  }

  public static <T> T createModel(ModelType<T> type) {
    ModelWrapper<T> wrapper = ModelFactory.createWrapper(type);
    return (wrapper == null) ? null : wrapper.getModel();
  }

}

