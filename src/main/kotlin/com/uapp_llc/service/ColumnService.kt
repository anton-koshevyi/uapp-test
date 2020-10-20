package com.uapp_llc.service

import com.uapp_llc.model.Column
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ColumnService {

  fun create(name: String): Column

  fun update(id: Long, name: String?): Column

  fun changeIndex(id: Long, index: Int): Column

  fun find(id: Long): Column

  fun findAll(pageable: Pageable): Page<Column>

  fun delete(id: Long)

}
