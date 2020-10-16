package com.uapp_llc.test.model.type;

import com.uapp_llc.model.Column;

public enum ColumnType implements ModelType<Column> {

  MONDAY,
  WEDNESDAY,
  FRIDAY;

  @Override
  public Class<Column> modelClass() {
    return Column.class;
  }

}
