package com.uapp_llc.test.model.column;

import com.uapp_llc.model.Column;
import com.uapp_llc.model.Project;

public class Friday extends Column {

  Friday() {
    super.setName("Friday tasks");
    super.setIndex(2);
    super.setProject(new Project());
  }

}
