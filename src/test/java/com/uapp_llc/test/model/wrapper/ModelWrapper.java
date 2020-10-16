package com.uapp_llc.test.model.wrapper;

import java.util.function.Consumer;

public interface ModelWrapper<T> {

  T getModel();

  ModelWrapper<T> with(Consumer<T> mutator);

}
