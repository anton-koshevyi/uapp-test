package com.uapp_llc.test.model.type

interface ModelType<T> {

  val name: String

  fun modelClass(): Class<T>

}
