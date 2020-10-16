package com.uapp_llc.test.model.mutator;

import java.util.function.Consumer;

import com.uapp_llc.model.Column;
import com.uapp_llc.model.Task;

public final class TaskMutators {

  private TaskMutators() {
  }

  public static Consumer<Task> id(Long v) {
    return m -> m.setId(v);
  }

  public static Consumer<Task> name(String v) {
    return m -> m.setName(v);
  }

  public static Consumer<Task> description(String v) {
    return m -> m.setDescription(v);
  }

  public static Consumer<Task> index(Integer v) {
    return m -> m.setIndex(v);
  }

  public static Consumer<Task> column(Column v) {
    return m -> m.setColumn(v);
  }

}
