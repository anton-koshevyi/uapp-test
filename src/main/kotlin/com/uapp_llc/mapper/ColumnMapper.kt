package com.uapp_llc.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import com.uapp_llc.dto.column.ColumnDto
import com.uapp_llc.model.Column

@Mapper
interface ColumnMapper {

  fun toDto(model: Column): ColumnDto


  companion object {

    @JvmField
    val INSTANCE: ColumnMapper = Mappers.getMapper(ColumnMapper::class.java)

  }

}
