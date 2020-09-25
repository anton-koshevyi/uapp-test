package com.uapp_llc.test.model.column;

import com.uapp_llc.model.Column;
import com.uapp_llc.test.model.ModelFactory;
import com.uapp_llc.test.model.ModelType;

public class ColumnFactory extends ModelFactory<Column> {

  @Override
  public Column createModel(ModelType<Column> type) {
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
