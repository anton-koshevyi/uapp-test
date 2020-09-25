package com.uapp_llc.test.comparator;

import java.util.Comparator;

import com.google.common.collect.ComparisonChain;

import com.uapp_llc.model.Column;
import com.uapp_llc.model.Task;

class TaskComparator implements Comparator<Task> {

  private final Comparator<Column> columnComparator;

  TaskComparator(Comparator<Column> columnComparator) {
    this.columnComparator = columnComparator;
  }

  @Override
  public int compare(Task left, Task right) {
    return ComparisonChain.start()
        .compare(left.getId(), right.getId())
        .compare(left.getCreatedAt(), right.getCreatedAt(), NotNullComparator.leftNotNull())
        .compare(left.getName(), right.getName())
        .compare(left.getDescription(), right.getDescription())
        .compare(left.getColumn(), right.getColumn(), columnComparator)
        .result();
  }

}
