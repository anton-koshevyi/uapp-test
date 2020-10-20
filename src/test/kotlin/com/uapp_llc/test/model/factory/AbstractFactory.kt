package com.uapp_llc.test.model.factory

import com.uapp_llc.test.model.type.ModelType

abstract class AbstractFactory<T> {

  abstract fun createModel(type: ModelType<T>): T

}
