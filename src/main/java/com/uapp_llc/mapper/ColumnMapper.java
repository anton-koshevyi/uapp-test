package com.uapp_llc.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.uapp_llc.dto.column.ColumnDto;
import com.uapp_llc.model.Column;

@Mapper
public interface ColumnMapper {

  ColumnMapper INSTANCE = Mappers.getMapper(ColumnMapper.class);

  @Mapping(target = "projectId", source = "project.id")
  ColumnDto toDto(Column model);

}
