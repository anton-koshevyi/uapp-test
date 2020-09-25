package com.uapp_llc.test.model.project;

import com.uapp_llc.model.Project;
import com.uapp_llc.test.model.ModelFactory;
import com.uapp_llc.test.model.ModelType;

public class ProjectFactory extends ModelFactory<Project> {

  @Override
  public Project createModel(ModelType<Project> type) {
    switch (Enum.valueOf(ProjectType.class, type.name())) {
      case DEFAULT:
        return new Project();
      default:
        return null;
    }
  }

}
