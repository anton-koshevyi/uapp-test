package com.uapp_llc.service

import kotlin.math.max
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.uapp_llc.exception.IllegalActionException
import com.uapp_llc.exception.NotFoundException
import com.uapp_llc.model.Column
import com.uapp_llc.repository.ColumnRepository

@Service
class ColumnServiceImpl @Autowired constructor(

    private val repository: ColumnRepository

) : ColumnService {

  @Transactional
  override fun create(name: String): Column {
    val entity = Column(
        name = name,
        index = repository.count()
    )
    return repository.save(entity)
  }

  @Transactional
  override fun update(id: Long, name: String?): Column {
    val entity: Column = this.find(id)

    if (name != null) {
      entity.name = name
    }

    return repository.save(entity)
  }

  @Transactional
  override fun changeIndex(id: Long, index: Int): Column {
    val entity: Column = this.find(id)
    return changeIndex(entity, index)
  }

  override fun find(id: Long): Column =
      repository.findById(id)
          ?: throw NotFoundException("No column for id '$id'")

  override fun findAll(pageable: Pageable): Page<Column> =
      repository.findAll(pageable)

  @Transactional
  override fun delete(id: Long) {
    val entity: Column = this.find(id)
    val last: Column = changeIndex(entity, max(0, repository.count() - 1))
    repository.delete(last)
  }

  private fun changeIndex(entity: Column, index: Int): Column {
    val actual: Int = entity.index

    if (actual == index) {
      return entity
    }

    if (index < 0 || index >= repository.count()) {
      throw IllegalActionException("New column index out of bounds: $index")
    }

    val columns: List<Column> = repository.findAllByOrderByIndex()

    if (actual > index) {
      for (i in index until actual) {
        val column: Column = columns[i]
        column.index = i + 1
        repository.save(column)
      }
    } else {
      for (i in index downTo actual + 1) {
        val column: Column = columns[i]
        column.index = i - 1
        repository.save(column)
      }
    }

    entity.index = index
    return repository.save(entity)
  }

}
