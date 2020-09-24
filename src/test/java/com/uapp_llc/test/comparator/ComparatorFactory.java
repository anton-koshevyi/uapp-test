package com.uapp_llc.test.comparator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

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
    }

    return (Comparator<T>) typeComparators.get(typeName);
  }

}
