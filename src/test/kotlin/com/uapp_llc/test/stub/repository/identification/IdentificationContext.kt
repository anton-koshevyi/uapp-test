package com.uapp_llc.test.stub.repository.identification

class IdentificationContext<T>(

    private var strategy: Identification<T>? = null

) : Identification<T> {

  fun setStrategy(strategy: Identification<T>) {
    this.strategy = strategy
  }

  override fun apply(entity: T) = strategy!!.apply(entity)

}
