package com.uapp_llc.mapper

import com.uapp_llc.dto.task.TaskDto
import com.uapp_llc.model.Task
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper(uses = [ColumnMapper::class])
interface TaskMapper {

  fun toDto(model: Task): TaskDto


  companion object {
    val INSTANCE: TaskMapper = Mappers.getMapper(TaskMapper::class.java)
  }

}
