package com.uapp_llc.test.model.type

import com.uapp_llc.model.Column

enum class ColumnType : ModelType<Column> {

  MONDAY,
  WEDNESDAY,
  FRIDAY;

  override fun modelClass(): Class<Column> = Column::class.java

}
