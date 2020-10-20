package com.uapp_llc.test.model.type

import com.uapp_llc.model.Task

enum class TaskType : ModelType<Task> {

  JOB,
  MEETING,
  GOLF;

  override fun modelClass(): Class<Task> = Task::class.java
}
