package com.uapp_llc.test.model.factory;

import com.uapp_llc.test.model.type.ModelType;
import com.uapp_llc.test.model.wrapper.ModelWrapper;

abstract class AbstractFactory<T> {

  abstract ModelWrapper<T> createWrapper(ModelType<T> type);

}
