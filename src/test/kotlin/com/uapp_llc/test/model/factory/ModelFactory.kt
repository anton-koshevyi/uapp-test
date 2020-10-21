package com.uapp_llc.test.model.factory

import com.uapp_llc.test.model.type.ModelType

class ModelFactory private constructor() {

  companion object {

    @JvmStatic
    fun <T> createModel(type: ModelType<T>): T {
      val modelClass: Class<T> = type.modelClass()
      return FactoryProducer.getFactory(modelClass).createModel(type)
    }

  }

}
