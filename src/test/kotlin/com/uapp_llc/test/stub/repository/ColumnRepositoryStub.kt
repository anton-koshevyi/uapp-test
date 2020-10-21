package com.uapp_llc.test.stub.repository;

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import com.uapp_llc.model.Column
import com.uapp_llc.repository.ColumnRepository
import com.uapp_llc.test.stub.repository.identification.Identification

class ColumnRepositoryStub(

    identification: Identification<Column>

) : IdentifyingRepositoryStub<Long, Column>(identification),
    ColumnRepository {

  override fun getId(entity: Column): Long? = entity.id

  override fun save(entity: Column): Column = super.save(entity)

  override fun findById(id: Long): Column? = super.find(id)

  override fun findAll(pageable: Pageable): Page<Column> = PageImpl(super.findAll())

  override fun findAllByOrderByIndex(): MutableList<Column> =
      super.findAll()
          .sortedBy { it.index }
          .toMutableList()

  override fun count(): Int = super.size()

  override fun delete(entity: Column) = super.delete(entity)

}
