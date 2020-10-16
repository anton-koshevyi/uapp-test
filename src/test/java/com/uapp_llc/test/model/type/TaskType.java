package com.uapp_llc.test.model.type;

import com.uapp_llc.model.Task;

public enum TaskType implements ModelType<Task> {

  JOB,
  MEETING,
  GOLF;

  @Override
  public Class<Task> modelClass() {
    return Task.class;
  }

}
