package com.uapp_llc.test.model.mutator;

import java.util.Arrays;
import java.util.function.Consumer;

import com.uapp_llc.model.Column;
import com.uapp_llc.model.Task;

public final class ColumnMutators {

  private ColumnMutators() {
  }

  public static Consumer<Column> id(Long v) {
    return m -> m.setId(v);
  }

  public static Consumer<Column> name(String v) {
    return m -> m.setName(v);
  }

  public static Consumer<Column> index(Integer v) {
    return m -> m.setIndex(v);
  }

  public static Consumer<Column> tasks(Task... v) {
    return m -> m.setTasks(Arrays.asList(v));
  }

}
