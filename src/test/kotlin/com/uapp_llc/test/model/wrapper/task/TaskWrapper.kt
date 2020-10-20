package com.uapp_llc.test.model.wrapper.task

import com.uapp_llc.model.Task
import com.uapp_llc.test.model.wrapper.ModelWrapper
import java.time.ZonedDateTime

abstract class TaskWrapper constructor(

    name: String,
    description: String,
    index: Int,
    createdAt: ZonedDateTime = ZonedDateTime.now()

) : ModelWrapper<Task>(Task(null, createdAt, name, description, index))
