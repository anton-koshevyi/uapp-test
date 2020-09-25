package com.uapp_llc.test.comparator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.uapp_llc.model.Column;
import com.uapp_llc.model.Project;
import com.uapp_llc.model.Task;

public final class ComparatorFactory {

  private static final Map<String, Comparator<?>> typeComparators = new HashMap<>();

  private ComparatorFactory() {
  }

  public static <T> Comparator<T> getComparator(Class<T> type) {
    if (type == null) {
      return null;
    }

    String typeName = type.getName();

    if (!typeComparators.containsKey(typeName)) {
      if (Project.class.equals(type)) {
        typeComparators.put(typeName, new ProjectComparator());
      }

      if (Column.class.equals(type)) {
        typeComparators.put(typeName, new ColumnComparator(
            ComparatorFactory.getComparator(Project.class)
        ));
      }

      if (Task.class.equals(type)) {
        typeComparators.put(typeName, new TaskComparator(
            ComparatorFactory.getComparator(Column.class)
        ));
      }
    }

    return (Comparator<T>) typeComparators.get(typeName);
  }

}
