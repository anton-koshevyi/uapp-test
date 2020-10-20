package com.uapp_llc.repository

import com.uapp_llc.model.Column
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ColumnRepository {

  fun save(entity: Column): Column

  fun findById(id: Long): Column?

  fun findAll(pageable: Pageable): Page<Column>

  fun findAllByOrderByIndex(): List<Column>

  fun count(): Int

  fun delete(entity: Column)

}
