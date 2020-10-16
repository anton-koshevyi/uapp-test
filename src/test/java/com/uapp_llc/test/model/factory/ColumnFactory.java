package com.uapp_llc.test.model.factory;

import com.uapp_llc.model.Column;
import com.uapp_llc.test.model.type.ColumnType;
import com.uapp_llc.test.model.type.ModelType;
import com.uapp_llc.test.model.wrapper.ModelWrapper;
import com.uapp_llc.test.model.wrapper.column.Friday;
import com.uapp_llc.test.model.wrapper.column.Monday;
import com.uapp_llc.test.model.wrapper.column.Wednesday;

class ColumnFactory extends AbstractFactory<Column> {

  @Override
  ModelWrapper<Column> createWrapper(ModelType<Column> type) {
    switch (Enum.valueOf(ColumnType.class, type.name())) {
      case MONDAY:
        return new Monday();
      case WEDNESDAY:
        return new Wednesday();
      case FRIDAY:
        return new Friday();
      default:
        return null;
    }
  }

}
