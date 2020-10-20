package com.uapp_llc.test.stub.repository;

import com.uapp_llc.model.Task
import com.uapp_llc.repository.TaskRepository
import com.uapp_llc.test.stub.repository.identification.Identification
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class TaskRepositoryStub(

    identification: Identification<Task>

) : IdentifyingRepositoryStub<Long, Task>(identification),
    TaskRepository {

  override fun getId(entity: Task): Long? = entity.id

  override fun save(entity: Task): Task = super.save(entity)

  override fun findByIdAndColumnId(id: Long, columnId: Long): Task? {
    return super.find {
      listOf(byId(id), byColumnId(columnId))
          .all { p -> p(it) }
    }
  }

  override fun findAllByColumnId(columnId: Long, pageable: Pageable): Page<Task> {
    val entities: List<Task> = super.findAll()
        .filter(byColumnId(columnId))
    return PageImpl(entities);
  }

  override fun delete(entity: Task) = super.delete(entity)


  private companion object {

    private fun byId(v: Long): (Task) -> Boolean = { it.id == v }

    private fun byColumnId(v: Long): (Task) -> Boolean = { it.column.id == v }

  }

}
