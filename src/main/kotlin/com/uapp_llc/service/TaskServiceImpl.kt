package com.uapp_llc.service

import com.uapp_llc.exception.IllegalActionException
import com.uapp_llc.exception.NotFoundException
import com.uapp_llc.model.Column
import com.uapp_llc.model.Task
import com.uapp_llc.repository.TaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.max

@Service
class TaskServiceImpl @Autowired constructor(

    private val repository: TaskRepository

) : TaskService {

  @Transactional
  override fun create(column: Column, name: String, description: String): Task {
    val entity = Task(
        name = name,
        description = description,
        index = column.tasks.size
    )
    entity.column = column
    return repository.save(entity)
  }

  @Transactional
  override fun update(id: Long, columnId: Long, name: String?, description: String?): Task {
    val entity: Task = this.find(id, columnId)

    if (name != null) {
      entity.name = name
    }

    if (description != null) {
      entity.description = description
    }

    return repository.save(entity)
  }

  @Transactional
  override fun move(id: Long, columnId: Long, newColumn: Column?, newIndex: Int?): Task {
    val entity: Task = this.find(id, columnId)

    if (newColumn != null) {
      entity.index = newColumn.tasks.size
      entity.column = newColumn
    }

    return if (newIndex != null) {
      changeIndex(entity, newIndex)
    } else {
      repository.save(entity)
    }
  }

  override fun find(id: Long, columnId: Long): Task {
    return repository.findByIdAndColumnId(id, columnId)
        ?: throw NotFoundException("No task for id '$id' and column id '$columnId'")
  }

  override fun findAll(columnId: Long, pageable: Pageable): Page<Task> {
    return repository.findAllByColumnId(columnId, pageable)
  }

  @Transactional
  override fun delete(id: Long, columnId: Long) {
    val entity: Task = this.find(id, columnId)
    val last: Task = changeIndex(entity, max(0, entity.column.tasks.size - 1))
    repository.delete(last)
  }

  private fun changeIndex(entity: Task, index: Int): Task {
    val actual: Int = entity.index

    if (actual == index) {
      return entity
    }

    val tasks: List<Task> = entity.column.tasks

    if (index < 0 || index >= tasks.size) {
      throw IllegalActionException("New task index out of bounds: $index")
    }

    if (actual > index) {
      for (i in index until actual) {
        val task: Task = tasks[i]
        task.index = i + 1
        repository.save(task)
      }
    } else {
      for (i in index downTo actual + 1) {
        val task: Task = tasks[i]
        task.index = i - 1
        repository.save(task)
      }
    }

    entity.index = index
    return repository.save(entity)
  }

}
