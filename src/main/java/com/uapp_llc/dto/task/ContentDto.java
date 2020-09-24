package com.uapp_llc.dto.task;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import com.uapp_llc.constraint.TaskDescription;
import com.uapp_llc.constraint.TaskName;

@Getter
@Setter
public class ContentDto {

  @NotNull(groups = CreateTask.class)
  @TaskName(groups = {CreateTask.class, UpdateTask.class})
  private String name;

  @NotNull(groups = CreateTask.class)
  @TaskDescription(groups = {CreateTask.class, UpdateTask.class})
  private String description;


  public interface CreateTask {
  }

  public interface UpdateTask {
  }

}
