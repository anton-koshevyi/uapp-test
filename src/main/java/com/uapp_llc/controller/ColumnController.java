package com.uapp_llc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.uapp_llc.dto.column.ColumnDto;
import com.uapp_llc.dto.column.ContentDto;
import com.uapp_llc.dto.column.MoveDto;
import com.uapp_llc.mapper.ColumnMapper;
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
  public Page<ColumnDto> getAll(@PathVariable Long projectId,
                                @PageableDefault(sort = "index") Pageable pageable) {
    Page<Column> columns = columnService.findAll(projectId, pageable);
    return columns.map(ColumnMapper.INSTANCE::toDto);
  }

  @PostMapping("/projects/{projectId}/columns")
  public ColumnDto create(@PathVariable Long projectId,
                          @RequestBody ContentDto dto) {
    Project project = projectService.find(projectId);
    Column column = columnService.create(
        project,
        dto.getName()
    );
    return ColumnMapper.INSTANCE.toDto(column);
  }

  @GetMapping("/projects/{projectId}/columns/{id}")
  public ColumnDto get(@PathVariable Long projectId,
                       @PathVariable Long id) {
    Column column = columnService.find(id, projectId);
    return ColumnMapper.INSTANCE.toDto(column);
  }

  @PatchMapping("/projects/{projectId}/columns/{id}")
  public ColumnDto update(@PathVariable Long projectId,
                          @PathVariable Long id,
                          @RequestBody ContentDto dto) {
    Column column = columnService.update(
        id,
        projectId,
        dto.getName()
    );
    return ColumnMapper.INSTANCE.toDto(column);
  }

  @PutMapping("/projects/{projectId}/columns/{id}")
  public ColumnDto changeIndex(@PathVariable Long projectId,
                               @PathVariable Long id,
                               @RequestBody MoveDto dto) {
    Column column = columnService.changeIndex(id, projectId, dto.getNewIndex());
    return ColumnMapper.INSTANCE.toDto(column);
  }

  @DeleteMapping("/projects/{projectId}/columns/{id}")
  public void delete(@PathVariable Long projectId,
                     @PathVariable Long id) {
    columnService.delete(id, projectId);
  }

}
