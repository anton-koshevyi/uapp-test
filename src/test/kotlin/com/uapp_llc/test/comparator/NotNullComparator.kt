package com.uapp_llc.test.comparator

import java.util.Comparator

class NotNullComparator<T> private constructor(

    private val leftNotNull: Boolean,
    private val rightNotNull: Boolean

) : Comparator<T> {

  override fun compare(left: T, right: T): Int {
    val result: Boolean =
        if (leftNotNull && rightNotNull) {
          (left != null) && (right != null)
        } else if (leftNotNull) {
          left != null
        } else {
          right != null
        }

    return if (result) 0 else 1
  }


  companion object {

    private val left: NotNullComparator<Any> =
        NotNullComparator(leftNotNull = true, rightNotNull = false)
    private val right: NotNullComparator<Any> =
        NotNullComparator(leftNotNull = false, rightNotNull = true)
    private val both: NotNullComparator<Any> =
        NotNullComparator(leftNotNull = true, rightNotNull = true)

    @JvmStatic
    fun <T> leftNotNull(): NotNullComparator<in T> {
      return left as NotNullComparator<in T>
    }

    @JvmStatic
    fun <T> rightNotNull(): NotNullComparator<in T> {
      return right as NotNullComparator<in T>
    }

    @JvmStatic
    fun <T> bothNotNull(): NotNullComparator<in T> {
      return both as NotNullComparator<in T>
    }

  }

}
