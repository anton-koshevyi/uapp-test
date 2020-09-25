package com.uapp_llc.test.model.task;

import com.uapp_llc.model.Task;
import com.uapp_llc.test.model.ModelFactory;
import com.uapp_llc.test.model.ModelType;

public class TaskFactory extends ModelFactory<Task> {

  @Override
  public Task createModel(ModelType<Task> type) {
    switch (Enum.valueOf(TaskType.class, type.name())) {
      case JOB:
        return new Job();
      case MEETING:
        return new Meeting();
      case GOLF:
        return new Golf();
      default:
        return null;
    }
  }

}
