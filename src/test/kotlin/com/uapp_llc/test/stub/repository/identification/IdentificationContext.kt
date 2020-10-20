package com.uapp_llc.test.stub.repository.identification

class IdentificationContext<T>(

    private var strategy: Identification<T>? = null

) : Identification<T> {

  fun setStrategy(strategy: (T) -> Unit) {
    this.strategy = object : Identification<T> {
      override fun apply(entity: T) = strategy(entity)
    }
  }

  override fun apply(entity: T) = strategy!!.apply(entity)

}
