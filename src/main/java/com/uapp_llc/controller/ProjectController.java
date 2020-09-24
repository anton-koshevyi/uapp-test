package com.uapp_llc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uapp_llc.model.Project;
import com.uapp_llc.service.ProjectService;

@RestController
public class ProjectController {

  private final ProjectService projectService;

  @Autowired
  public ProjectController(ProjectService projectService) {
    this.projectService = projectService;
  }

  @GetMapping("/projects/{id}")
  public Project get(@PathVariable Long id) {
    Project project = projectService.find(id);
    return project;
  }

  @PostMapping("/projects/{id}")
  public Project create() {
    Project project = projectService.create();
    return project;
  }

  @DeleteMapping("/projects/{id}")
  public void delete(@PathVariable Long id) {
    projectService.delete(id);
  }

}
