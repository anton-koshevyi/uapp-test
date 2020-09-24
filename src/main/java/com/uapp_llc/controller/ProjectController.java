package com.uapp_llc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uapp_llc.service.ProjectService;

@RestController
public class ProjectController {

  private final ProjectService projectService;

  @Autowired
  public ProjectController(ProjectService projectService) {
    this.projectService = projectService;
  }

  @PostMapping("/projects")
  public void create() {
    projectService.create();
  }

  @DeleteMapping("/projects/{id}")
  public void delete(@PathVariable Long id) {
    projectService.delete(id);
  }

}
