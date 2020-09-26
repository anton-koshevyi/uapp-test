package com.uapp_llc.test.model.task;

import java.time.ZonedDateTime;

import com.uapp_llc.model.Column;
import com.uapp_llc.model.Task;

class Meeting extends Task {

  Meeting() {
    super.setCreatedAt(ZonedDateTime.now());
    super.setName("Meeting");
    super.setDescription("Meet John");
    super.setIndex(1);
    super.setColumn(new Column());
  }

}
