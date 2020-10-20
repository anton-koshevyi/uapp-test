package com.uapp_llc.test.model.factory

import com.uapp_llc.model.Column
import com.uapp_llc.test.model.type.ColumnType
import com.uapp_llc.test.model.type.ModelType
import com.uapp_llc.test.model.wrapper.ModelWrapper
import com.uapp_llc.test.model.wrapper.column.Friday
import com.uapp_llc.test.model.wrapper.column.Monday
import com.uapp_llc.test.model.wrapper.column.Wednesday

class ColumnFactory : AbstractFactory<Column>() {

  override fun createModel(type: ModelType<Column>): Column {
    val wrapper: ModelWrapper<Column> =
        when (type as ColumnType) {
          ColumnType.MONDAY -> Monday()
          ColumnType.WEDNESDAY -> Wednesday()
          ColumnType.FRIDAY -> Friday()
        }
    return wrapper.model
  }

}
