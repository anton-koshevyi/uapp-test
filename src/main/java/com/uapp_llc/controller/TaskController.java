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
import com.uapp_llc.model.Task;
import com.uapp_llc.service.ColumnService;
import com.uapp_llc.service.TaskService;

@RestController
public class TaskController {

  private final ColumnService columnService;
  private final TaskService taskService;

  @Autowired
  public TaskController(ColumnService columnService, TaskService taskService) {
    this.columnService = columnService;
    this.taskService = taskService;
  }

  @GetMapping("/projects/{projectId}/columns/{columnId}/tasks")
  public Page<Task> getAll(@PathVariable Long columnId,
                           Pageable pageable) {
    Page<Task> tasks = taskService.findAll(columnId, pageable);
    return tasks;
  }

  @PostMapping("/projects/{projectId}/columns/{columnId}/tasks")
  public Task create(@PathVariable Long projectId,
                     @PathVariable Long columnId,
                     @RequestBody Task dto) {
    Column column = columnService.find(projectId, columnId);
    Task task = taskService.create(
        column,
        dto.getName(),
        dto.getDescription()
    );
    return task;
  }

  @GetMapping("/projects/{projectId}/columns/{columnId}/tasks/{id}")
  public Task get(@PathVariable Long columnId,
                  @PathVariable Long id) {
    Task task = taskService.find(id, columnId);
    return task;
  }

  @PatchMapping("/projects/{projectId}/columns/{columnId}/tasks/{id}")
  public Task update(@PathVariable Long columnId,
                     @PathVariable Long id,
                     @RequestBody Task dto) {
    Task task = taskService.update(
        id,
        columnId,
        dto.getName(),
        dto.getDescription()
    );
    return task;
  }

  @PutMapping("/projects/{projectId}/columns/{columnId}/tasks/{id}")
  public Task attachPosition(@PathVariable Long columnId,
                             @PathVariable Long id,
                             @RequestParam Integer position) {
    Task task = taskService.changePosition(id, columnId, position);
    return task;
  }

  @PutMapping("/projects/{projectId}/columns/{columnId}/tasks/{id}")
  public Task attachColumn(@PathVariable Long projectId,
                           @PathVariable("columnId") Long actualId,
                           @PathVariable Long id,
                           @RequestParam("column") Long changeId) {
    Column change = columnService.find(changeId, projectId);
    Task task = taskService.changeColumn(id, actualId, change);
    return task;
  }

  @DeleteMapping("/projects/{projectId}/columns/{columnId}/tasks/{id}")
  public void delete(@PathVariable Long columnId,
                     @PathVariable Long id) {
    taskService.delete(id, columnId);
  }

}
