package com.uapp_llc.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class CollectionUtil {

  private CollectionUtil() {
  }

  public static <T> List<T> move(List<T> source, int fromIndex, int toIndex) {
    Objects.requireNonNull(source, "Source must not be null");

    if (fromIndex == toIndex) {
      return source;
    }

    List<T> list = new ArrayList<>(source);

    if (fromIndex < toIndex) {
      Collections.rotate(list.subList(fromIndex, toIndex + 1), -1);
    } else {
      Collections.rotate(list.subList(toIndex, fromIndex + 1), 1);
    }

    return list;
  }

}
