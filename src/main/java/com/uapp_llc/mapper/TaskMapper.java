package com.uapp_llc.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.uapp_llc.dto.task.TaskDto;
import com.uapp_llc.model.Task;

@Mapper(uses = ColumnMapper.class)
public interface TaskMapper {

  TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

  TaskDto toDto(Task model);

}
