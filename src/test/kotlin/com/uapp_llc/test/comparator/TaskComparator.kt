package com.uapp_llc.test.comparator

import com.google.common.collect.ComparisonChain
import com.uapp_llc.model.Column
import com.uapp_llc.model.Task
import java.util.Comparator

class TaskComparator(

    private val columnComparator: Comparator<in Column>

) : Comparator<Task> {

  override fun compare(left: Task?, right: Task?): Int {
    return ComparisonChain.start()
        .compare(left?.id as Comparable<*>, right?.id as Comparable<*>)
        .compare(left.createdAt, right.createdAt, NotNullComparator.leftNotNull())
        .compare(left.name as Comparable<*>, right.name as Comparable<*>)
        .compare(left.description as Comparable<*>, right.description as Comparable<*>)
        .compare(left.index, right.index)
        .compare(left.column, right.column, columnComparator)
        .result()
  }

}
