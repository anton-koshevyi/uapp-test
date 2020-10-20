package com.uapp_llc.repository

import com.uapp_llc.model.Column
import com.uapp_llc.repository.jpa.ColumnJpaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository

@Repository
class ColumnRepositoryImpl @Autowired constructor(

    private val delegate: ColumnJpaRepository

) : ColumnRepository {

  override fun save(entity: Column): Column = delegate.save(entity)

  override fun findById(id: Long): Column = delegate.findById(id).orElse(null)

  override fun findAll(pageable: Pageable): Page<Column> = delegate.findAll(pageable)

  override fun findAllByOrderByIndex(): List<Column> = delegate.findAll(Sort.by("index"))

  override fun count(): Int = delegate.count().toInt()

  override fun delete(entity: Column) = delegate.delete(entity)

}
