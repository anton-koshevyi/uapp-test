package com.uapp_llc.test.model.factory

import com.uapp_llc.model.Task
import com.uapp_llc.test.model.type.ModelType
import com.uapp_llc.test.model.type.TaskType
import com.uapp_llc.test.model.wrapper.ModelWrapper
import com.uapp_llc.test.model.wrapper.task.Golf
import com.uapp_llc.test.model.wrapper.task.Job
import com.uapp_llc.test.model.wrapper.task.Meeting

class TaskFactory : AbstractFactory<Task>() {

  override fun createModel(type: ModelType<Task>): Task {
    val wrapper: ModelWrapper<Task> =
        when (type as TaskType) {
          TaskType.JOB -> Job()
          TaskType.MEETING -> Meeting()
          TaskType.GOLF -> Golf()
        }
    return wrapper.model
  }

}
