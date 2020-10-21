package com.uapp_llc.test.comparator

import java.util.Comparator
import com.uapp_llc.model.Column
import com.uapp_llc.model.Task

object ComparatorFactory {

  private val typeComparators: MutableMap<String, Comparator<*>> = mutableMapOf()

  @JvmStatic
  fun <T> getComparator(type: Class<in T>): Comparator<in T?> {
    val typeName: String = type.name

    if (!typeComparators.containsKey(typeName)) {
      when (type) {
        Column::class.java -> typeComparators[typeName] = ColumnComparator()
        Task::class.java -> typeComparators[typeName] = TaskComparator(
            getComparator(Column::class.java)
        )
      }
    }

    return typeComparators[typeName] as Comparator<in T?>
  }

}
