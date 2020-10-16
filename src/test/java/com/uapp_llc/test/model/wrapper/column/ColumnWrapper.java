package com.uapp_llc.test.model.wrapper.column;

import com.uapp_llc.model.Column;
import com.uapp_llc.test.model.mutator.ColumnMutators;
import com.uapp_llc.test.model.wrapper.AbstractWrapper;

abstract class ColumnWrapper extends AbstractWrapper<Column> {

  protected ColumnWrapper(String name, Integer index) {
    super(new Column());
    super
        .with(ColumnMutators.name(name))
        .with(ColumnMutators.index(index));
  }

}
