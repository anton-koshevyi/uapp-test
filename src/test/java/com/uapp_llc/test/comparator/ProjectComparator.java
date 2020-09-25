package com.uapp_llc.test.comparator;

import java.util.Comparator;

import com.google.common.collect.ComparisonChain;

import com.uapp_llc.model.Project;

class ProjectComparator implements Comparator<Project> {

  @Override
  public int compare(Project left, Project right) {
    return ComparisonChain.start()
        .compare(left.getId(), right.getId())
        .result();
  }

}
