package com.uapp_llc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uapp_llc.model.Column;
import com.uapp_llc.model.Project;
import com.uapp_llc.service.ColumnService;
import com.uapp_llc.service.ProjectService;

@RestController
public class ColumnController {

  private final ProjectService projectService;
  private final ColumnService columnService;

  @Autowired
  public ColumnController(ProjectService projectService, ColumnService columnService) {
    this.projectService = projectService;
    this.columnService = columnService;
  }

  @GetMapping("/projects/{projectId}/columns")
  public Page<Column> getAll(@PathVariable Long projectId,
                             Pageable pageable) {
    Page<Column> columns = columnService.findAll(projectId, pageable);
    return columns;
  }

  @PostMapping("/projects/{projectId}/columns")
  public Column create(@PathVariable Long projectId,
                       @RequestBody Column dto) {
    Project project = projectService.find(projectId);
    Column column = columnService.create(
        project,
        dto.getName()
    );
    return column;
  }

  @GetMapping("/projects/{projectId}/columns/{id}")
  public Column get(@PathVariable Long projectId,
                    @PathVariable Long id) {
    Column column = columnService.find(id, projectId);
    return column;
  }

  @PatchMapping("/projects/{projectId}/columns/{id}")
  public Column update(@PathVariable Long projectId,
                       @PathVariable Long id,
                       @RequestBody Column dto) {
    Column column = columnService.update(
        id,
        projectId,
        dto.getName()
    );
    return column;
  }

  @PutMapping("/projects/{projectId}/columns/{id}")
  public Column attachPosition(@PathVariable Long projectId,
                               @PathVariable Long id,
                               @RequestParam Integer position) {
    Column column = columnService.changePosition(id, projectId, position);
    return column;
  }

  @DeleteMapping("/projects/{projectId}/columns/{id}")
  public void delete(@PathVariable Long projectId,
                     @PathVariable Long id) {
    columnService.delete(id, projectId);
  }

}
