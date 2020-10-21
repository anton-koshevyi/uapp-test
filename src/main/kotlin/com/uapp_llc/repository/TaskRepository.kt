package com.uapp_llc.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import com.uapp_llc.model.Task

interface TaskRepository {

  fun save(entity: Task): Task

  fun findByIdAndColumnId(id: Long, columnId: Long): Task?

  fun findAllByColumnId(columnId: Long, pageable: Pageable): Page<Task>

  fun delete(entity: Task)

}
