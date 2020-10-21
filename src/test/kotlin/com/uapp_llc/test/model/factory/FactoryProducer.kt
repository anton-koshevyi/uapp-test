package com.uapp_llc.test.model.factory

import com.uapp_llc.model.Column
import com.uapp_llc.model.Task

object FactoryProducer {

  private val typeFactories: MutableMap<String, AbstractFactory<*>> = mutableMapOf()

  @JvmStatic
  fun <T> getFactory(type: Class<T>): AbstractFactory<T> {
    val typeName: String = type.typeName

    if (!typeFactories.containsKey(typeName)) {
      when (type) {
        Column::class.java -> typeFactories[typeName] = ColumnFactory()
        Task::class.java -> typeFactories[typeName] = TaskFactory()
      }
    }

    return typeFactories[typeName] as AbstractFactory<T>
  }

}

