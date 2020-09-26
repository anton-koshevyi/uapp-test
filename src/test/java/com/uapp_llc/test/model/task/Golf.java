package com.uapp_llc.test.model.task;

import java.time.ZonedDateTime;

import com.uapp_llc.model.Column;
import com.uapp_llc.model.Task;

class Golf extends Task {

  Golf() {
    super.setCreatedAt(ZonedDateTime.now());
    super.setName("Golf");
    super.setDescription("Play golf");
    super.setIndex(2);
    super.setColumn(new Column());
  }

}
