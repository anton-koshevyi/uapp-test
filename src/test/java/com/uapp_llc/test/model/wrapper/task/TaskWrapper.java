package com.uapp_llc.test.model.wrapper.task;

import com.uapp_llc.model.Task;
import com.uapp_llc.test.model.mutator.TaskMutators;
import com.uapp_llc.test.model.wrapper.AbstractWrapper;

abstract class TaskWrapper extends AbstractWrapper<Task> {

  protected TaskWrapper(String name, String description, Integer index) {
    super(new Task());
    super
        .with(TaskMutators.name(name))
        .with(TaskMutators.description(description))
        .with(TaskMutators.index(index));
  }

}
