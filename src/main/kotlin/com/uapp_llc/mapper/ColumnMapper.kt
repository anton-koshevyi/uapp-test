package com.uapp_llc.mapper

import com.uapp_llc.dto.column.ColumnDto
import com.uapp_llc.model.Column
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface ColumnMapper {

  fun toDto(model: Column): ColumnDto


  companion object {
    val INSTANCE: ColumnMapper = Mappers.getMapper(ColumnMapper::class.java)
  }

}
