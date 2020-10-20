package com.uapp_llc.test.comparator

import java.util.Comparator
import org.apache.commons.lang3.builder.CompareToBuilder
import com.uapp_llc.model.Column

class ColumnComparator : Comparator<Column> {

  override fun compare(left: Column?, right: Column?): Int {
    return CompareToBuilder()
        .append(left?.id, right?.id)
        .append(left?.name, right?.name)
        .append(left?.index, right?.index)
        .toComparison()
  }

}
