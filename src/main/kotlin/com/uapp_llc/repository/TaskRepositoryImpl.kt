package com.uapp_llc.repository

import com.uapp_llc.model.Task
import com.uapp_llc.repository.jpa.TaskJpaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class TaskRepositoryImpl @Autowired constructor(

    private val delegate: TaskJpaRepository

) : TaskRepository {

  override fun save(entity: Task): Task =
      delegate.save(entity)

  override fun findByIdAndColumnId(id: Long, columnId: Long): Task? =
      delegate.findByIdAndColumnId(id, columnId)

  override fun findAllByColumnId(columnId: Long, pageable: Pageable): Page<Task> =
      delegate.findAllByColumnId(columnId, pageable)

  override fun delete(entity: Task) =
      delegate.delete(entity)

}
