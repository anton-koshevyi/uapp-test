package com.uapp_llc.test.stub.repository

import com.uapp_llc.test.stub.repository.identification.Identification

abstract class IdentifyingRepositoryStub<ID, T>(

    private val identification: Identification<T>

) : AbstractRepositoryStub<ID, T>() {

  protected abstract fun getId(entity: T): ID?

  override fun save(id: ID?, entity: T): T {
    if (id == null || !super.exists(id)) {
      identification.apply(entity)
      val entityId: ID? = this.getId(entity)
      logger.debug("Applied id '{}' to entity: {}", entityId, entity)
      return super.save(entityId, entity)
    }

    return super.save(id, entity)
  }

  open fun save(entity: T): T {
    val id: ID? = this.getId(entity)
    return this.save(id, entity)
  }

  open fun delete(entity: T) {
    val id: ID? = this.getId(entity)
    super.delete(id, entity)
  }

}
