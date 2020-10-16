package com.uapp_llc.test.model.factory;

import java.util.HashMap;
import java.util.Map;

import com.uapp_llc.model.Column;
import com.uapp_llc.model.Task;

final class FactoryProducer {

  private static final Map<String, AbstractFactory<?>> typeFactories = new HashMap<>();

  private FactoryProducer() {
  }

  static <T> AbstractFactory<T> getFactory(Class<T> type) {
    String typeName = type.getName();

    if (!typeFactories.containsKey(typeName)) {
      if (Column.class.equals(type)) {
        typeFactories.put(typeName, new ColumnFactory());
      }

      if (Task.class.equals(type)) {
        typeFactories.put(typeName, new TaskFactory());
      }
    }

    return (AbstractFactory<T>) typeFactories.get(typeName);
  }

}
