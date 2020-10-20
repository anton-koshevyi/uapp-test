package com.uapp_llc.test.comparator

import com.google.common.collect.ComparisonChain
import com.uapp_llc.model.Column
import java.util.Comparator

class ColumnComparator : Comparator<Column> {

  override fun compare(left: Column?, right: Column?): Int {
    return ComparisonChain.start()
        .compare(left?.id as Comparable<*>, right?.id as Comparable<*>)
        .compare(left.name as Comparable<*>, right.name as Comparable<*>)
        .compare(left.index as Comparable<*>, right.index as Comparable<*>)
        .result()
  }

}
