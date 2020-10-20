package com.uapp_llc.test.comparator

import java.util.Comparator
import org.apache.commons.lang3.builder.CompareToBuilder
import com.uapp_llc.model.Column
import com.uapp_llc.model.Task

class TaskComparator(

    private val columnComparator: Comparator<in Column>

) : Comparator<Task> {

  override fun compare(left: Task?, right: Task?): Int {
    return CompareToBuilder()
        .append(left?.id, right?.id)
        .append(left?.name, right?.name)
        .append(left?.description, right?.description)
        .append(left?.index, right?.index)
        .append(left?.column, right?.column, columnComparator)
        .toComparison()
  }

}
