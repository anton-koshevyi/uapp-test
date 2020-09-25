package com.uapp_llc.test.model.task;

import java.time.ZonedDateTime;

import com.uapp_llc.model.Column;
import com.uapp_llc.model.Task;

class Job extends Task {

  Job() {
    super.setCreatedAt(ZonedDateTime.now());
    super.setName("Job");
    super.setDescription("Go to job");
    super.setColumn(new Column());
  }

}
