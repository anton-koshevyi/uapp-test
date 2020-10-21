package com.uapp_llc.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import com.uapp_llc.dto.task.TaskDto
import com.uapp_llc.model.Task

@Mapper(uses = [ColumnMapper::class])
interface TaskMapper {

  fun toDto(model: Task): TaskDto


  companion object {

    @JvmField
    val INSTANCE: TaskMapper = Mappers.getMapper(TaskMapper::class.java)

  }

}
