package com.uapp_llc.test.model.factory;

import com.uapp_llc.model.Task;
import com.uapp_llc.test.model.type.ModelType;
import com.uapp_llc.test.model.type.TaskType;
import com.uapp_llc.test.model.wrapper.ModelWrapper;
import com.uapp_llc.test.model.wrapper.task.Golf;
import com.uapp_llc.test.model.wrapper.task.Job;
import com.uapp_llc.test.model.wrapper.task.Meeting;

class TaskFactory extends AbstractFactory<Task> {

  @Override
  ModelWrapper<Task> createWrapper(ModelType<Task> type) {
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
