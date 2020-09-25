package com.uapp_llc.test.model;

import java.util.HashMap;
import java.util.Map;

import com.uapp_llc.model.Column;
import com.uapp_llc.model.Project;
import com.uapp_llc.model.Task;
import com.uapp_llc.test.model.column.ColumnFactory;
import com.uapp_llc.test.model.project.ProjectFactory;
import com.uapp_llc.test.model.task.TaskFactory;

public final class ModelFactoryProducer {

  private static final Map<String, ModelFactory<?>> typeFactories = new HashMap<>();

  private ModelFactoryProducer() {
  }

  public static <T> ModelFactory<T> getFactory(Class<T> type) {
    if (type == null) {
      return null;
    }

    String typeName = type.getName();

    if (!typeFactories.containsKey(typeName)) {
      if (Project.class.equals(type)) {
        typeFactories.put(typeName, new ProjectFactory());
      }

      if (Column.class.equals(type)) {
        typeFactories.put(typeName, new ColumnFactory());
      }

      if (Task.class.equals(type)) {
        typeFactories.put(typeName, new TaskFactory());
      }
    }

    return (ModelFactory<T>) typeFactories.get(typeName);
  }

}
