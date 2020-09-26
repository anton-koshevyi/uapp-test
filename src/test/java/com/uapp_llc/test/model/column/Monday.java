package com.uapp_llc.test.model.column;

import com.uapp_llc.model.Column;
import com.uapp_llc.model.Project;

class Monday extends Column {

  Monday() {
    super.setName("Monday tasks");
    super.setIndex(0);
    super.setProject(new Project());
  }

}
