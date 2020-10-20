package com.uapp_llc.test.comparator

import com.uapp_llc.model.Column
import com.uapp_llc.model.Task
import java.util.Comparator

class ComparatorFactory private constructor() {

  companion object {

    private val typeComparators: MutableMap<String, Comparator<*>> = mutableMapOf()

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

}
