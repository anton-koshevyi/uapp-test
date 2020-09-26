package com.uapp_llc.test.comparator;

import java.util.Comparator;

import com.google.common.collect.ComparisonChain;

import com.uapp_llc.model.Column;
import com.uapp_llc.model.Project;

class ColumnComparator implements Comparator<Column> {

  private final Comparator<Project> projectComparator;

  ColumnComparator(Comparator<Project> projectComparator) {
    this.projectComparator = projectComparator;
  }

  @Override
  public int compare(Column left, Column right) {
    return ComparisonChain.start()
        .compare(left.getId(), right.getId())
        .compare(left.getName(), right.getName())
        .compare(left.getIndex(), right.getIndex())
        .compare(left.getProject(), right.getProject(), projectComparator)
        .result();
  }

}
